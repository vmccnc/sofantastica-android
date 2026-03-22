package com.furniture.duet.data.model.furniture

import androidx.room.Relation
import com.furniture.duet.data.db.entity.FurnitureImageEntity

data class FurnitureFabricDto(
    val furnitureId: Int,
    val fabricId: Int,
    val furnitureName: String,
    val fabricName: String,
    val basePrice: Double,
    val fabricPrice: Double,
    val totalPrice: Double,
    val description: String,
    val imageUrl: String,
    val fabricUrl: String,
    @Relation(parentColumn = "furnitureId", entityColumn = "furnitureId", entity = FurnitureImageEntity::class)
    val imageUrls: List<FurnitureImageEntity>?,
    val modelUrl: String,
    val category: String,
    val isFavorite: Boolean
)
