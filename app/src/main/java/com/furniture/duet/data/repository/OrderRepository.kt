package com.furniture.duet.data.repository

import com.furniture.duet.data.model.OrderDto

interface OrderRepository {
    suspend fun createOrder(order: OrderDto): OrderDto
    suspend fun getOrders(userId: String): List<OrderDto>
}
