package com.furniture.duet.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import com.furniture.duet.data.model.OrderDto
import com.furniture.duet.data.repository.OrderRepository
import com.furniture.duet.domain.usecase.GetOrdersUseCase

class GetOrdersUseCaseTest {
    private val repo = object : OrderRepository {
        override suspend fun createOrder(order: OrderDto) = order
        override suspend fun getOrders(userId: String) = listOf(OrderDto(1, userId))
    }

    private val useCase = GetOrdersUseCase(repo)

    @Test
    fun invoke_returnsOrders() = runBlocking {
        val result = useCase("u1")
        assertEquals(1, result.size)
        assertEquals("u1", result.first().userId)
    }
}
