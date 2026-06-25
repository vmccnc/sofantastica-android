package com.furniture.duet.data.model.account

data class AccountDto(
    val userId: String,
    val customerType: String = "",
    val firstAndLastName: String = "",
    val companyName: String = "",
    val unn: String = "",
    val phone: String = "",
    val email: String,
    val address: String = "",
    val city: String = "",
    val country: String = "",
    val postCode: String = "",
    val role: String = ""
)
