package pl.sofantastica.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.model.OrderDto
import pl.sofantastica.data.repository.OrderRepository

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
