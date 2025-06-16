package pl.sofantastica.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.repository.FavoritesRepository

class GetFavoritesUseCaseTest {
    private val repo = object : FavoritesRepository {
        override suspend fun getFavorites(userId: String) = listOf(
            FurnitureDto(1, "Chair", 0.0, "", "", emptyList(), "", "")
        )
        override suspend fun addFavorite(userId: String, furnitureId: Long) {}
        override suspend fun removeFavorite(userId: String, furnitureId: Long) {}
        override suspend fun isFavorite(userId: String, furnitureId: Long) = false
    }

    private val useCase = GetFavoritesUseCase(repo)

    @Test
    fun invoke_returnsList() = runBlocking {
        val result = useCase("u1")
        assertEquals(1, result.size)
        assertEquals("Chair", result.first().name)
    }
}
