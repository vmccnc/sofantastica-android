package pl.sofantastica.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.FurnitureFabricDto
import pl.sofantastica.data.repository.FavoritesRepository

class GetFavoritesUseCaseTest {
    private val repo = object : FavoritesRepository {
        override suspend fun loadFavorite(): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun getFavorites(): List<FurnitureCatalogModel> {
            TODO("Not yet implemented")
        }

        override suspend fun getFavoriteFurnitureDetail(id: Int): FurnitureFabricDto {
            TODO("Not yet implemented")
        }

        override suspend fun addFavorite(furnitureId: Int): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun removeFavorite(furnitureId: Int): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun isFavorite(furnitureId: Int): Boolean {
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
