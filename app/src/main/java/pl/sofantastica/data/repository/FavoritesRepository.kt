package pl.sofantastica.data.repository

import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.data.model.FurnitureDto

interface FavoritesRepository {
    suspend fun loadFavorite(): Boolean
    suspend fun getFavorites(): List<FurnitureCatalogModel>
    suspend fun getFavoriteFurnitureDetail(id: Int): FurnitureDto
    suspend fun addFavorite(furnitureId: Int): Boolean
    suspend fun removeFavorite(furnitureId: Int): Boolean
    suspend fun isFavorite(furnitureId: Int): Boolean
}
