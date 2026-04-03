package com.furniture.duet.data.model.cart

data class CartItemUpdateResponse(
    val id: Int,
    val userId: String,
    val furnitureId: Int,
    val furnitureName: String,
    val furnitureUrl: String,
    val fabricId: Int,
    val fabricName: String,
    val fabricUrl: String,
    val priceSofaWithFabric: Int,
    val quantity: Int,
    val checkCalculation: String,
    val priceForQuantity: Int
)