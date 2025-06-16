package pl.sofantastica.data.repository

import pl.sofantastica.data.model.OrderDto

interface OrderRepository {
    suspend fun createOrder(order: OrderDto): OrderDto
    suspend fun getOrders(userId: String): List<OrderDto>
}
