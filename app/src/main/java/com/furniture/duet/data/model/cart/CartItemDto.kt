package com.furniture.duet.data.model.cart

import com.furniture.duet.data.model.furniture.CategoryDto

data class CartItemDto(
    val id: Int,
    val userId: String,
    val furnitureId: Int,
    val furnitureName: String,
    val furnitureUrl: String,
    val fabricId: Int,
    val fabricName: String,
    val fabricUrl: String,
    val priceSofaWithFabric: Double,
    val quantity: Int,
    val checkCalculation: String,
    val category: CategoryDto
)
