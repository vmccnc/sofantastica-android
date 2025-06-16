package pl.sofantastica.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.model.FabricDto
import pl.sofantastica.data.repository.FabricRepository

class GetFabricsUseCaseTest {
    private val repo = object : FabricRepository {
        override suspend fun getFabrics(): List<FabricDto> = listOf(FabricDto(1, "Cotton", "A"))
        override suspend fun getFabric(id: Int) = FabricDto(id, "Name$id", "B")
        override suspend fun getFabricsBySupplier(supplier: String) = emptyList<FabricDto>()
    }

    private val useCase = GetFabricsUseCase(repo)

    @Test
    fun invoke_returnsList() = runBlocking {
        val result = useCase()
        assertEquals(1, result.size)
        assertEquals("Cotton", result.first().name)
    }
}
