package pl.sofantastica.data.repository

import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.model.FurnitureDto
import retrofit2.HttpException
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService
) : FavoritesRepository {
    override suspend fun getFavorites(userId: String): List<FurnitureDto> {
        val response = api.listFavorites(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        throw HttpException(response)
    }

    override suspend fun addFavorite(userId: String, furnitureId: Long) {
        val response = api.addFavorite(userId, furnitureId)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    override suspend fun removeFavorite(userId: String, furnitureId: Long) {
        val response = api.removeFavorite(userId, furnitureId)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    override suspend fun isFavorite(userId: String, furnitureId: Long): Boolean {
        val response = api.isFavorite(userId, furnitureId)
        if (response.isSuccessful) {
            return response.body() ?: false
        }
        throw HttpException(response)
    }
}
