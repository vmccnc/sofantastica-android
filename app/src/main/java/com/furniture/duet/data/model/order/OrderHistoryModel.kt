package com.furniture.duet.data.model.order

data class OrderHistoryModel(
    val orders: List<OrderModel>,
    val page: Int,
    val isLast: Boolean
)