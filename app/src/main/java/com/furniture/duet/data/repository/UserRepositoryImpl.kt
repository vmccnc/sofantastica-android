package com.furniture.duet.data.repository

import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.model.account.AccountModel
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import com.furniture.duet.domain.exceptions.SignUpException
import com.furniture.duet.domain.exceptions.WrongLoginOrPasswordException
import com.furniture.duet.domain.usecase.account.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val api: RetrofitApiService,
    private val connectionManager: InternetConnectionManager
): UserRepository {
    override suspend fun getAccountInfo() = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        auth.currentUser ?: IsNotAuthorizeException()
        val userInfo = api.getUser(auth.currentUser?.uid ?: "")
        if (userInfo.isSuccessful) userInfo.body()
        else throw Exception("No such user")
    }

    override suspend fun createAccount(
        email: String,
        password: String
    ): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (_: Exception) {
            throw SignUpException()
        }

        api.createUser(
            AccountModel(
                userId = auth.currentUser?.uid ?: "",
                email = email
            )
        )
    }

    override suspend fun updateAccount(
        customerType: String,
        firstAndLastName: String,
        companyName: String,
        unn: String,
        phone: String,
        email: String,
        address: String,
        city: String,
        country: String,
        postCode: String
    ): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        auth.currentUser ?: throw IsNotAuthorizeException()

        val userId = auth.currentUser?.uid ?: ""

        api.updateUser(
            userId,
            AccountModel(
                userId = userId,
                customerType = customerType,
                firstAndLastName = firstAndLastName,
                companyName = companyName,
                unn = unn,
                phone = phone,
                email = email,
                address = address,
                city = city,
                country = country,
                postCode = postCode
            )
        )
    }
}