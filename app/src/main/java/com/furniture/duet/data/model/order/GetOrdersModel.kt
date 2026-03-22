package com.furniture.duet.data.model.order

data class GetOrdersModel(
    val orders: List<OrderDto>,
    val totalElements: Int,
    val totalPages: Int
)