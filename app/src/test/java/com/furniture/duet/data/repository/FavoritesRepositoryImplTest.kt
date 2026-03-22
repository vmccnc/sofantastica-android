package com.furniture.duet.data.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.model.cart.AddCartItemResponse
import com.furniture.duet.data.model.cart.CartItemDto
import com.furniture.duet.data.model.cart.CartItemUpdateDto
import com.furniture.duet.data.model.fabric.FabricDto
import com.furniture.duet.data.model.furniture.FurnitureDto
import com.furniture.duet.data.model.order.OrderDto
import com.furniture.duet.data.model.PriceDto
import com.furniture.duet.data.model.cart.AddCartItemRequest
import com.furniture.duet.data.model.cart.CartDto
import retrofit2.Response

class FavoritesRepositoryImplTest {
    private val favorites = listOf(
        FurnitureDto(1, "Chair", 50.0, "desc", "img", emptyList(), "model", "chairs"),
        FurnitureDto(2, "Table", 80.0, "desc", "img", emptyList(), "model", "tables")
    )

    private var added = mutableListOf<FurnitureDto>()

    private val api = object : RetrofitApiService {
        override suspend fun listFavorites(userId: String): Response<List<FurnitureDto>> = Response.success(favorites + added)
        override suspend fun isFavorite(userId: String, furnitureId: Long): Response<Boolean> =
            Response.success((favorites + added).any { it.id.toLong() == furnitureId })
        override suspend fun addFavorite(userId: String, furnitureId: Long): Response<Unit> {
            added.add(FurnitureDto(furnitureId.toInt(), "New", 0.0, "", "", emptyList(), "", ""))
            return Response.success(Unit)
        }
        override suspend fun removeFavorite(userId: String, furnitureId: Long): Response<Unit> {
            added.removeAll { it.id.toLong() == furnitureId }
            return Response.success(Unit)
        }
        override suspend fun listFurniturs(): Response<List<FurnitureDto>> = Response.success(emptyList())
        override suspend fun listCategories(): Response<List<String>> = Response.success(emptyList())
        override suspend fun getFurnitureDetail(id: Int): Response<FurnitureDto> = Response.success(favorites.first())
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

        override suspend fun listOrders(userId: String): Response<List<OrderDto>> = throw UnsupportedOperationException()
        override suspend fun createOrder(order: OrderDto): Response<OrderDto> = throw UnsupportedOperationException()
    }

    private val repository = FavoritesRepositoryImpl(
        api,
        favoriteDao = TODO(),
        furnitureDao = TODO(),
        auth = TODO(),
        connectionManager = TODO()
    )

    @Test
    fun getFavorites_returnsList() = runBlocking {
        val result = repository.getFavorites()
        assertEquals(2, result.size)
    }

    @Test
    fun addAndRemoveFavorite_modifiesList() = runBlocking {
        repository.addFavorite(5)
        var list = repository.getFavorites()
        assertEquals(3, list.size)
        repository.removeFavorite(5)
        list = repository.getFavorites()
        assertEquals(2, list.size)
    }
}
