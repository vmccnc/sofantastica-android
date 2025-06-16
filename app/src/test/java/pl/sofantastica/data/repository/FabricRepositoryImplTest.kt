package pl.sofantastica.data.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.model.FabricDto
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
        override suspend fun listFurniturs(): Response<List<pl.sofantastica.data.model.FurnitureDto>> = throw UnsupportedOperationException()
        override suspend fun listCategories(): Response<List<String>> = throw UnsupportedOperationException()
        override suspend fun getFurnitureDetail(id: Int): Response<pl.sofantastica.data.model.FurnitureDto> = throw UnsupportedOperationException()
        override suspend fun listOrders(userId: String): Response<List<pl.sofantastica.data.model.OrderDto>> = throw UnsupportedOperationException()
        override suspend fun createOrder(order: pl.sofantastica.data.model.OrderDto): Response<pl.sofantastica.data.model.OrderDto> = throw UnsupportedOperationException()
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
