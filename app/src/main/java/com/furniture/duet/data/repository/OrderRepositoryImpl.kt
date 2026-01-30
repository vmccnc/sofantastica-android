package com.furniture.duet.data.repository

import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.model.OrderDto
import retrofit2.HttpException
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService
) : OrderRepository {
    override suspend fun createOrder(order: OrderDto): OrderDto {
        val response = api.createOrder(order)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return body
        }
        throw HttpException(response)
    }

    override suspend fun getOrders(userId: String): List<OrderDto> {
        val response = api.listOrders(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        throw HttpException(response)
    }
}
