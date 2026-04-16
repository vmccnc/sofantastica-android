package com.furniture.duet.data.model.order

data class OrderResponseDto(
    val orders: List<OrderDto>,
    val totalElements: Int,
    val totalPages: Int
)