package com.furniture.duet.data.model.order

import com.furniture.duet.data.db.entity.CartItemEntity
import java.time.LocalDate

data class OrderDto(
    val id: Int? = null,
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
    val orderDate: String,
    val status: String,
    val items: List<OrderItemDto>,
    val totalPrice: Double
)
