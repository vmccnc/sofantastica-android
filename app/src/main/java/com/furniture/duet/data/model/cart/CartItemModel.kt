package com.furniture.duet.data.model.cart

data class CartItemModel(
    val id: Int,
    val furnitureId: Int,
    val fabricId: Int,
    val quantity: Int,
    val furnitureUrl: String,
    val fabricUrl: String,
    val furnitureName: String,
    val fabricName: String,
    val price: Double,
    val isFavorite: Boolean
)