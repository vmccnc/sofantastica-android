package com.furniture.duet.data.model.order

data class CreateOrderModel(
    val userId: String,
    val customerType: String,
    val firstAndLastName: String,
    val companyName: String,
    val unn: String,
    val email: String,
    val phone: String,
    val address: String,
    val city: String,
    val postCode: String,
    val country: String,
    val typeOfDelivery: String,
    val typeOfPayment: String,
    val status: String = "NEW"
)
