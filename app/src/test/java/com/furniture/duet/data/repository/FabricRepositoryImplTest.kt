package com.furniture.duet.data.repository

import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.db.dao.FabricDao
import com.furniture.duet.data.db.entity.FabricEntity
import com.furniture.duet.data.db.entity.SupplierEntity
import com.furniture.duet.data.model.cart.AddCartItemResponse
import com.furniture.duet.data.model.cart.CartItemDto
import com.furniture.duet.data.model.cart.CartItemUpdateDto
import com.furniture.duet.data.model.fabric.FabricDto
import com.furniture.duet.data.model.furniture.FurnitureDto
import com.furniture.duet.data.model.OrderDto
import com.furniture.duet.data.model.PriceDto
import com.furniture.duet.data.model.cart.AddCartItemRequest
import com.furniture.duet.data.repository.FabricRepositoryImpl
import com.furniture.duet.data.model.cart.CartDto
import retrofit2.Response

class FabricRepositoryImplTest {
    private val fabrics = listOf(
        FabricDto(
            1, "Cotton", "A",
            fabricUrl = "",
            fabricUrlBig = "",
            price = 0.0
        ),
        FabricDto(2, "Silk", "B",
            fabricUrl = "",
            fabricUrlBig = "",
            price = 0.0)
    )

    private val connectionManager = InternetConnectionManager(ApplicationProvider.getApplicationContext())

    private val fakeApi = object : RetrofitApiService {
        override suspend fun listFabrics(): Response<List<FabricDto>> = Response.success(fabrics)
        override suspend fun getFabricDetail(id: Int): Response<FabricDto> = Response.success(fabrics[0])
        override suspend fun listFabricsBySupplier(supplier: String): Response<List<FabricDto>> =
            Response.success(fabrics.filter { it.supplier == supplier })
        override suspend fun listSuppliers(): Response<List<String>> = Response.success(listOf("A", "B"))
        override suspend fun listPopularFabrics(): Response<List<FabricDto>> = Response.success(fabrics)
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

        override suspend fun listFurniturs(): Response<List<FurnitureDto>> = throw UnsupportedOperationException()
        override suspend fun listCategories(): Response<List<String>> = throw UnsupportedOperationException()
        override suspend fun getFurnitureDetail(id: Int): Response<FurnitureDto> = throw UnsupportedOperationException()
        override suspend fun listOrders(userId: String): Response<List<OrderDto>> = throw UnsupportedOperationException()
        override suspend fun createOrder(order: OrderDto): Response<OrderDto> = throw UnsupportedOperationException()
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
    }


    private val fakeDao = object : FabricDao {
        override suspend fun getFabrics(): List<FabricDto> {
            return emptyList()
        }

        override suspend fun getFabricsBySupplier(supplier: String): List<FabricDto> {
            return emptyList()
        }

        override suspend fun getFabric(id: Int): FabricDto? {
            return fabrics.find { it.id == id }
        }

        override suspend fun insertAll(items: List<FabricEntity>) {
            TODO("Not yet implemented")
        }

        override suspend fun deleteAllFabrics() {
            TODO("Not yet implemented")
        }

        override suspend fun getSuppliers(): List<String> {
            TODO("Not yet implemented")
        }

        override suspend fun insertSuppliers(items: List<SupplierEntity>) {
            TODO("Not yet implemented")
        }

        override suspend fun deleteAllSuppliers() {
            TODO("Not yet implemented")
        }
    }

    private val repository = FabricRepositoryImpl(
        fakeApi,
        fakeDao,
        connectionManager
    )

    @Test
    fun getFabrics_returnsList() = runBlocking {
        var list = repository.getFabricsBySupplier("A")
        assertEquals(1, list.size)
        list = repository.getFabricsBySupplier("B")
        assertEquals(1, list.size)
        list = repository.getFabricsBySupplier("C")
        assertEquals(0, list.size)
    }

    @Test
    fun getFabric_returnsDetail() = runBlocking {
        val item = repository.getFabric(3)
        assertNotEquals(null, item)
        assertEquals(3, item?.id)
    }

    @Test
    fun getFabricsBySupplier_filters() = runBlocking {
        val result = repository.getFabricsBySupplier("A")
        assertEquals(1, result.size)
        assertEquals("A", result.first().supplier)
    }
}
