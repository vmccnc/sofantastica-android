package pl.sofantastica.data.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.model.AddCartItemResponse
import pl.sofantastica.data.model.CartItemDto
import pl.sofantastica.data.model.CartItemUpdateDto
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.PriceDto
import pl.sofantastica.data.model.SuccessResponse
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
        override suspend fun listFabrics(): Response<List<pl.sofantastica.data.model.FabricDto>> = throw UnsupportedOperationException()
        override suspend fun getFabricDetail(id: Int): Response<pl.sofantastica.data.model.FabricDto> = throw UnsupportedOperationException()
        override suspend fun listSuppliers(): Response<List<String>> = throw UnsupportedOperationException()
        override suspend fun listFabricsBySupplier(supplier: String): Response<List<pl.sofantastica.data.model.FabricDto>> = throw UnsupportedOperationException()
        override suspend fun listPopularFabrics(): Response<List<pl.sofantastica.data.model.FabricDto>> = throw UnsupportedOperationException()
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

        override suspend fun listOrders(userId: String): Response<List<pl.sofantastica.data.model.OrderDto>> = throw UnsupportedOperationException()
        override suspend fun createOrder(order: pl.sofantastica.data.model.OrderDto): Response<pl.sofantastica.data.model.OrderDto> = throw UnsupportedOperationException()
    }

    private val repository = FavoritesRepositoryImpl(api)

    @Test
    fun getFavorites_returnsList() = runBlocking {
        val result = repository.getFavorites("u1")
        assertEquals(2, result.size)
    }

    @Test
    fun addAndRemoveFavorite_modifiesList() = runBlocking {
        repository.addFavorite("u1", 5)
        var list = repository.getFavorites("u1")
        assertEquals(3, list.size)
        repository.removeFavorite("u1", 5)
        list = repository.getFavorites("u1")
        assertEquals(2, list.size)
    }
}
