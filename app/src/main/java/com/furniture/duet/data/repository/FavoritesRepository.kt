package com.furniture.duet.data.repository

import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.model.furniture.FurnitureFabricDto

interface FavoritesRepository {
    suspend fun loadFavorites()
    suspend fun getFavorites(): List<FurnitureCatalogModel>
    suspend fun addFavorite(
        id: Int,
        name: String,
        basePrice: Int,
        imageUrl: String
    )
    suspend fun removeFavorite(furnitureId: Int)
    suspend fun isFavorite(furnitureId: Int): Boolean
    suspend fun clear()
}
