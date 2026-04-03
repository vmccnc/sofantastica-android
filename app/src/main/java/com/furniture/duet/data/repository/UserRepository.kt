package com.furniture.duet.data.repository

import com.furniture.duet.data.model.account.AccountModel

interface UserRepository {
    suspend fun getAccountInfo(): AccountModel?
    suspend fun createAccount(
        email: String,
        firstAndLastName: String,
        phoneNumber: String,
        password: String
    )
    suspend fun updateAccount(accountModel: AccountModel)
}