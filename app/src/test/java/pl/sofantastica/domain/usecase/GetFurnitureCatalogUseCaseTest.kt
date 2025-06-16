package pl.sofantastica.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.repository.FurnitureRepository

class GetFurnitureCatalogUseCaseTest {
    private val repo = object : FurnitureRepository {
        override suspend fun getFurniture(): List<FurnitureDto> = listOf(
            FurnitureDto(1, "Chair", 10.0, "", "", emptyList(), "", "chairs")
        )
        override suspend fun getCategories() = emptyList<pl.sofantastica.data.model.CategoryDto>()
        override suspend fun getFurnitureDetail(id: Int) = FurnitureDto(id, "Name$id", 0.0, "", "", emptyList(), "", "chairs")
    }

    private val useCase = GetFurnitureCatalogUseCase(repo)

    @Test
    fun invoke_returnsRepositoryData() = runBlocking {
        val result = useCase()
        assertEquals(1, result.size)
        assertEquals("Chair", result.first().name)
    }
}
