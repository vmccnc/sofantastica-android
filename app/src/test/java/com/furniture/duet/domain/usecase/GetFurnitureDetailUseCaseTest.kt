package com.furniture.duet.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import com.furniture.duet.data.model.furniture.CategoryDto
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import com.furniture.duet.data.repository.FurnitureRepository
import com.furniture.duet.domain.usecase.furnitures.GetFurnitureDetailUseCase

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
