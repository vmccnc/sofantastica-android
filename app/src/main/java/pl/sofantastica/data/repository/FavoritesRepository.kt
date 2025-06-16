package pl.sofantastica.data.repository

import pl.sofantastica.data.model.FurnitureDto

interface FavoritesRepository {
    suspend fun getFavorites(userId: String): List<FurnitureDto>
    suspend fun addFavorite(userId: String, furnitureId: Long)
    suspend fun removeFavorite(userId: String, furnitureId: Long)
    suspend fun isFavorite(userId: String, furnitureId: Long): Boolean
}
