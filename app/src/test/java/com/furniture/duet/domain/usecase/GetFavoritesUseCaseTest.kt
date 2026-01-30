package com.furniture.duet.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.domain.usecase.GetFavoritesUseCase

class GetFavoritesUseCaseTest {
    private val repo = object : FavoritesRepository {

        override suspend fun loadFavorites() {
            TODO("Not yet implemented")
        }

        override suspend fun getFavorites(): List<FurnitureCatalogModel> {
            TODO("Not yet implemented")
        }

        override suspend fun getFavoriteFurnitureDetail(id: Int): FurnitureFabricDto {
            TODO("Not yet implemented")
        }

        override suspend fun addFavorite(furnitureId: Int) {
            TODO("Not yet implemented")
        }

        override suspend fun removeFavorite(furnitureId: Int) {
            TODO("Not yet implemented")
        }

        override suspend fun isFavorite(furnitureId: Int): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun clear() {
            TODO("Not yet implemented")
        }
    }

    private val useCase = GetFavoritesUseCase(repo)

    @Test
    fun invoke_returnsList() = runBlocking {
        val result = useCase()
        assertEquals(1, result.size)
        assertEquals("Chair", result.first().name)
    }
}
