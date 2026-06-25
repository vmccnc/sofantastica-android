package com.furniture.duet.data.repository

import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.model.account.AccountDto
import com.furniture.duet.data.model.account.AccountModel
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import com.furniture.duet.domain.exceptions.SignUpException
import com.furniture.duet.domain.exceptions.WrongLoginOrPasswordException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val api: RetrofitApiService,
    private val connectionManager: InternetConnectionManager
): UserRepository {

    private companion object {
        var userRole: String = ""
    }

    override suspend fun signIn(email: String, password: String): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        try {
            auth.signInWithEmailAndPassword(email, password)
        } catch (_: Exception) {
        }
        if (auth.currentUser == null)
            throw WrongLoginOrPasswordException()
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()

        try {
            auth.createUserWithEmailAndPassword(email, password)
        } catch (_: Exception) {

        }
        if (auth.currentUser == null)
            throw SignUpException()
        api.addUser(
            AccountDto(
                userId = auth.currentUser!!.uid,
                customerType = "",
                firstAndLastName = "",
                companyName = "",
                unn = "",
                phone = "",
                email = email,
                address = "",
                city = "",
                country = "",
                postCode = "",
                role = "USER"
            )
        )
    }

    override suspend fun getUserData(): AccountModel? = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        if (auth.currentUser == null)
            return@withContext null
        val userId = auth.currentUser!!.uid
        val response = api.getUser(userId)
        var userInfo: AccountModel? = null
        if (response.isSuccessful) {
            response.body()?.let {
                userRole = it.role
                userInfo = AccountModel(
                    firstAndLastName = it.firstAndLastName,
                    companyName = it.companyName,
                    unn = it.unn,
                    phone = it.phone,
                    email = it.email,
                    address = it.address,
                    city = it.city,
                    country = it.country,
                    postCode = it.postCode
                )
            }
        }
        userInfo
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
        val user = auth.currentUser ?: throw IsNotAuthorizeException()

        val userId = user.uid

        api.updateUser(
            userId,
            AccountDto(
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
                postCode = postCode,
                role = userRole
            )
        )
    }
}