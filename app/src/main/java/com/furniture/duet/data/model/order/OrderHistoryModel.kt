package com.furniture.duet.data.model.order

data class OrderHistoryModel(
    val orders: List<OrderDto>,
    val page: Int,
    val isLast: Boolean
)