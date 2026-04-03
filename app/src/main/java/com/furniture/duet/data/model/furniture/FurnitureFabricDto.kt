package com.furniture.duet.data.model.furniture

import androidx.room.Relation

data class FurnitureFabricDto(
    val furnitureId: Int,
    val fabricId: Int,
    val furnitureName: String,
    val furnitureUrl: String,
    val fabricName: String,
    val basePrice: Int,
    val fabricPrice: Int,
    val totalPrice: Int,
    val description: String,
    val imageUrl: String,
    val fabricUrl: String,
    val imageUrls: List<String>,
    val modelUrl: String,
    val category: String,
    val isFavorite: Boolean
)
