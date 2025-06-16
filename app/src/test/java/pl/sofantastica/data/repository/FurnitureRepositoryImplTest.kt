package pl.sofantastica.data.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureDto
import retrofit2.Response

class FurnitureRepositoryImplTest {

    private val fakeApi = object : RetrofitApiService {
        override suspend fun listFurniturs(): Response<List<FurnitureDto>> =
            Response.success(listOf(
                FurnitureDto(1, "Chair", 50.0, "Desc", "img", emptyList(), "model", "chairs"),
                FurnitureDto(2, "Table", 80.0, "Desc", "img", emptyList(), "model", "tables")
            ))

        override suspend fun listCategories(): Response<List<String>> =
            Response.success(listOf("chairs", "tables"))

        override suspend fun getFurnitureDetail(id: Int): Response<FurnitureDto> =
            Response.success(FurnitureDto(id, "Chair$id", 60.0, "Desc", "img", emptyList(), "model", "chairs"))

        override suspend fun listFabrics(): Response<List<pl.sofantastica.data.model.FabricDto>> =
            throw UnsupportedOperationException()
        override suspend fun getFabricDetail(id: Int): Response<pl.sofantastica.data.model.FabricDto> =
            throw UnsupportedOperationException()
        override suspend fun listSuppliers(): Response<List<String>> =
            throw UnsupportedOperationException()
        override suspend fun listFabricsBySupplier(supplier: String): Response<List<pl.sofantastica.data.model.FabricDto>> =
            throw UnsupportedOperationException()
        override suspend fun listPopularFabrics(): Response<List<pl.sofantastica.data.model.FabricDto>> =
            throw UnsupportedOperationException()
        override suspend fun listOrders(userId: String): Response<List<pl.sofantastica.data.model.OrderDto>> =
            throw UnsupportedOperationException()
        override suspend fun createOrder(order: pl.sofantastica.data.model.OrderDto): Response<pl.sofantastica.data.model.OrderDto> =
            throw UnsupportedOperationException()
    }

    private val repository = FurnitureRepositoryImpl(fakeApi)

    @Test
    fun getFurniture_returnsApiData() = runBlocking {
        val result = repository.getFurniture()
        assertEquals(2, result.size)
        assertEquals("Chair", result.first().name)
    }

    @Test
    fun getCategories_mapsToDto() = runBlocking {
        val categories = repository.getCategories()
        assertEquals(listOf(CategoryDto("chairs"), CategoryDto("tables")), categories)
    }

    @Test
    fun getFurnitureDetail_returnsItem() = runBlocking {
        val item = repository.getFurnitureDetail(5)
        assertEquals(5, item.id)
        assertEquals("Chair5", item.name)
    }
}
