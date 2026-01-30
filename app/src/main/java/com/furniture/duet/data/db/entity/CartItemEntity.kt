package com.furniture.duet.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val furnitureId: Int,
    val fabricId: Int,
    val quantity: Int
)
