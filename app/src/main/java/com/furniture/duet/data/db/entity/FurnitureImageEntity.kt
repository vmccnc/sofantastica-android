package com.furniture.duet.data.db.entity

import androidx.room.Entity

@Entity(tableName = "furniture_image",
    primaryKeys = ["furnitureId", "imageUrl"])
data class FurnitureImageEntity(
    val furnitureId: Int,
    val imageUrl: String
)