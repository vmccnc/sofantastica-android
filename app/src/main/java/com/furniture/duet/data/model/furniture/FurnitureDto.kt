package com.furniture.duet.data.model.furniture

import java.time.LocalDateTime

data class FurnitureDto(
    val id: Int,
    val name: String,
    val basePrice: Int,
    val description: String,
    val imageUrl: String,
    val imageUrls: List<String>,
    val modelUrl: String,
    val category: CategoryDto,
    val createdAt: String,
    val updatedAt: String,
    val urls36: List<String>
)