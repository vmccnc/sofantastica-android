package pl.sofantastica.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.FurnitureFabricDto
import pl.sofantastica.data.repository.FurnitureRepository
import pl.sofantastica.domain.usecase.furnitures.GetFurnitureCatalogUseCase

class GetFurnitureCatalogUseCaseTest {
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

    private val useCase = GetFurnitureCatalogUseCase(repo)

    @Test
    fun invoke_returnsRepositoryData() = runBlocking {
        val result = useCase(
            minPrice = 0.0,
            maxPrice = 2000.0,
            category = null
        )
        assertEquals(1, result.size)
        assertEquals("Chair", result.first().name)
    }
}
