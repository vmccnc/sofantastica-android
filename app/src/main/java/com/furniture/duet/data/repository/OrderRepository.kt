package com.furniture.duet.data.repository

import com.furniture.duet.data.model.order.OrderDto
import com.furniture.duet.data.model.order.OrderHistoryModel

interface OrderRepository {
    suspend fun createOrder(
        customerType: String,
        firstAndLastName: String,
        companyName: String,
        unn: String,
        email: String,
        phone: String,
        address: String,
        city: String,
        postCode: String,
        country: String,
        typeOfDelivery: String,
        typeOfPayment: String
    ): Boolean
    suspend fun getOrders(page: Int): OrderHistoryModel
}
