package com.furniture.duet.data.repository

import com.furniture.duet.data.model.account.AccountModel

interface UserRepository {
    suspend fun signIn(
        email: String,
        password: String
    )
    suspend fun signUp(
        email: String,
        password: String
    )
    suspend fun getUserData(): AccountModel?
    suspend fun updateAccount(
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
    )
}