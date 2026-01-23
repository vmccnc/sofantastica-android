package pl.sofantastica.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.FurnitureFabricDto
import pl.sofantastica.data.repository.FurnitureRepository

class GetFurnitureDetailUseCaseTest {
    private val repo = object : FurnitureRepository {
        override suspend fun getMaxPrice(): Double {
            TODO("Not yet implemented")
        }

        override suspend fun loadFurniture(): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun getFurniture(
            minPrice: Double,
            maxPrice: Double,
            category: String?
        ): List<FurnitureCatalogModel> {
            TODO("Not yet implemented")
        }

        override suspend fun getCategories() = emptyList<CategoryDto>()
        override suspend fun getFurnitureDetail(id: Int): FurnitureFabricDto {
            TODO("Not yet implemented")
        }
    }

    private val useCase = GetFurnitureDetailUseCase(
        repo,
        favoritesRepository = TODO()
    )

    @Test
    fun invoke_returnsDetailFromRepository() = runBlocking {
        val result = useCase(5)
        assertEquals(5, result.furnitureId)
        assertEquals("Item5", result.name)
    }
}
