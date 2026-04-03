package com.furniture.duet.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.furniture.duet.data.model.furniture.CategoryDto

@Entity(tableName = "cart")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val furnitureId: Int,
    val furnitureName: String,
    val furnitureUrl: String,
    val fabricId: Int,
    val fabricName: String,
    val fabricUrl: String,
    val basePrice: Int,
    val fabricPrice: Int,
    val quantity: Int
)