package com.furniture.duet.data.repository

import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.model.account.AccountModel
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import com.furniture.duet.domain.exceptions.WrongLoginOrPasswordException
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
        else null
    }

    override suspend fun createAccount(
        email: String,
        firstAndLastName: String,
        phoneNumber: String,
        password: String
    ): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        if (!email.endsWith("@gmail.com")) {
            throw WrongLoginOrPasswordException()
        }
        auth.createUserWithEmailAndPassword(email, password).await()
        auth.currentUser ?: throw WrongLoginOrPasswordException()

        api.createUser(
            AccountModel(
                userId = auth.currentUser?.uid ?: "",
                email = email,
                firstAndLastName = firstAndLastName,
                phone = phoneNumber
            )
        )
    }

    override suspend fun updateAccount(accountModel: AccountModel): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        auth.currentUser ?: IsNotAuthorizeException()
        api.updateUser(
            auth.currentUser?.uid ?: "",
            accountModel
        )
    }
}