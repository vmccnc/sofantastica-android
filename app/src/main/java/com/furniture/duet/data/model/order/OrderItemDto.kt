package com.furniture.duet.data.model.order

data class OrderItemDto(
    val id: Int,
    val furnitureId: Int,
    val furnitureName: String,
    val furnitureUrl: String,
    val fabricId: Int,
    val fabricName: String,
    val fabricUrl: String,
    val finalPrice: Int,
    val order: String = ""
)