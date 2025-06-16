package pl.sofantastica.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.repository.FurnitureRepository

class GetFurnitureDetailUseCaseTest {
    private val repo = object : FurnitureRepository {
        override suspend fun getFurniture(): List<FurnitureDto> = emptyList()
        override suspend fun getCategories() = emptyList<pl.sofantastica.data.model.CategoryDto>()
        override suspend fun getFurnitureDetail(id: Int) = FurnitureDto(id, "Item$id", 0.0, "", "", emptyList(), "", "chairs")
    }

    private val useCase = GetFurnitureDetailUseCase(repo)

    @Test
    fun invoke_returnsDetailFromRepository() = runBlocking {
        val result = useCase(5)
        assertEquals(5, result.id)
        assertEquals("Item5", result.name)
    }
}
