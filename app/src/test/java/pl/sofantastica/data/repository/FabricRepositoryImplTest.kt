package pl.sofantastica.data.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.model.AddCartItemResponse
import pl.sofantastica.data.model.CartItemDto
import pl.sofantastica.data.model.CartItemUpdateDto
import pl.sofantastica.data.model.FabricDto
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.PriceDto
import pl.sofantastica.data.model.SuccessResponse
import retrofit2.Response

class FabricRepositoryImplTest {
    private val fabrics = listOf(
        FabricDto(1, "Cotton", "A"),
        FabricDto(2, "Silk", "B")
    )

    private val fakeApi = object : RetrofitApiService {
        override suspend fun listFabrics(): Response<List<FabricDto>> = Response.success(fabrics)
        override suspend fun getFabricDetail(id: Int): Response<FabricDto> = Response.success(FabricDto(id, "Name$id", "S"))
        override suspend fun listFabricsBySupplier(supplier: String): Response<List<FabricDto>> =
            Response.success(fabrics.filter { it.supplier == supplier })
        override suspend fun listSuppliers(): Response<List<String>> = Response.success(listOf("A", "B"))
        override suspend fun listPopularFabrics(): Response<List<FabricDto>> = Response.success(fabrics)
        override suspend fun getPrice(furnitureId: Int, fabricId: Int): Response<PriceDto> {
            TODO("Not yet implemented")
        }

        override suspend fun getCart(userId: String): Response<List<CartItemDto>> {
            TODO("Not yet implemented")
        }

        override suspend fun addCartItem(item: CartItemDto): Response<AddCartItemResponse> {
            TODO("Not yet implemented")
        }

        override suspend fun updateCartItem(
            cartItemId: Int,
            item: CartItemUpdateDto
        ): Response<SuccessResponse> {
            TODO("Not yet implemented")
        }

        override suspend fun deleteCartItem(id: Int): Response<Unit> {
            TODO("Not yet implemented")
        }

        override suspend fun listFurniturs(): Response<List<pl.sofantastica.data.model.FurnitureDto>> = throw UnsupportedOperationException()
        override suspend fun listCategories(): Response<List<String>> = throw UnsupportedOperationException()
        override suspend fun getFurnitureDetail(id: Int): Response<pl.sofantastica.data.model.FurnitureDto> = throw UnsupportedOperationException()
        override suspend fun listOrders(userId: String): Response<List<pl.sofantastica.data.model.OrderDto>> = throw UnsupportedOperationException()
        override suspend fun createOrder(order: pl.sofantastica.data.model.OrderDto): Response<pl.sofantastica.data.model.OrderDto> = throw UnsupportedOperationException()
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

    private val repository = FabricRepositoryImpl(fakeApi)

    @Test
    fun getFabrics_returnsList() = runBlocking {
        val list = repository.getFabrics()
        assertEquals(2, list.size)
    }

    @Test
    fun getFabric_returnsDetail() = runBlocking {
        val item = repository.getFabric(3)
        assertEquals(3, item.id)
    }

    @Test
    fun getFabricsBySupplier_filters() = runBlocking {
        val result = repository.getFabricsBySupplier("A")
        assertEquals(1, result.size)
        assertEquals("A", result.first().supplier)
    }
}
