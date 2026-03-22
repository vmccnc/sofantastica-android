package com.furniture.duet.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "furniture")
data class FurnitureEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val category: String,
    val basePrice: Double,
    val description: String,
    val imageUrl: String,
    val modelUrl: String
)
