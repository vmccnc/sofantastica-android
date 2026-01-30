package com.furniture.duet.data.repository

import com.furniture.duet.data.repository.OrderRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.model.cart.AddCartItemResponse
import com.furniture.duet.data.model.cart.CartItemDto
import com.furniture.duet.data.model.cart.CartItemUpdateDto
import com.furniture.duet.data.model.furniture.FurnitureDto
import com.furniture.duet.data.model.OrderDto
import com.furniture.duet.data.model.PriceDto
import com.furniture.duet.data.model.cart.AddCartItemRequest
import com.furniture.duet.data.model.cart.CartDto
import com.furniture.duet.data.model.fabric.FabricDto
import retrofit2.Response

class OrderRepositoryImplTest {

    private val orders = listOf(
        OrderDto(1, "u1"),
        OrderDto(2, "u1")
    )

    private val fakeApi = object : RetrofitApiService {
        override suspend fun listOrders(userId: String): Response<List<OrderDto>> = Response.success(orders)
        override suspend fun createOrder(order: OrderDto): Response<OrderDto> = Response.success(order.copy(id = 3))
        override suspend fun listFavorites(userId: String): Response<List<FurnitureDto>> {
            TODO("Not yet implemented")
        }

        override suspend fun isFavorite(userId: String, furnitureId: Long): Response<Boolean> {
            TODO("Not yet implemented")
        }

        override suspend fun addFavorite(userId: String, furnitureId: Long): Response<Unit> {
            TODO("Not yet implemented")
        }

        override suspend fun removeFavorite(userId: String, furnitureId: Long): Response<Unit> {
            TODO("Not yet implemented")
        }

        override suspend fun listFurniturs(): Response<List<FurnitureDto>> = throw UnsupportedOperationException()
        override suspend fun listCategories(): Response<List<String>> = throw UnsupportedOperationException()
        override suspend fun getFurnitureDetail(id: Int): Response<FurnitureDto> = throw UnsupportedOperationException()
        override suspend fun listFabrics(): Response<List<FabricDto>> = throw UnsupportedOperationException()
        override suspend fun getFabricDetail(id: Int): Response<FabricDto> = throw UnsupportedOperationException()
        override suspend fun listSuppliers(): Response<List<String>> = throw UnsupportedOperationException()
        override suspend fun listFabricsBySupplier(supplier: String): Response<List<FabricDto>> = throw UnsupportedOperationException()
        override suspend fun listPopularFabrics(): Response<List<FabricDto>> = throw UnsupportedOperationException()
        override suspend fun getPrice(furnitureId: Int, fabricId: Int): Response<PriceDto> {
            TODO("Not yet implemented")
        }

        override suspend fun getCart(userId: String): Response<CartDto> {
            TODO("Not yet implemented")
        }

        override suspend fun addCartItem(item: AddCartItemRequest): Response<AddCartItemResponse> {
            TODO("Not yet implemented")
        }

        override suspend fun updateCartItem(
            cartItemId: Int,
            item: CartItemUpdateDto
        ): Response<CartItemDto> {
            TODO("Not yet implemented")
        }

        override suspend fun updateCartItemQuantity(
            cartItemId: Int,
            item: CartItemUpdateDto
        ): Response<CartItemDto> {
            TODO("Not yet implemented")
        }

        override suspend fun deleteCartItem(id: Int): Response<Unit> {
            TODO("Not yet implemented")
        }
    }

    private val repository = OrderRepositoryImpl(fakeApi)

    @Test
    fun createOrder_returnsCreatedOrder() = runBlocking {
        val order = OrderDto(userId = "u1")
        val result = repository.createOrder(order)
        assertEquals(3, result.id)
    }

    @Test
    fun getOrders_returnsList() = runBlocking {
        val result = repository.getOrders("u1")
        assertEquals(2, result.size)
    }
}
