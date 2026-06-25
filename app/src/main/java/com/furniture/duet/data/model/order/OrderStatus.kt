package com.furniture.duet.data.model.order

import com.furniture.duet.R

sealed class OrderStatus(val textId: Int) {
    data object PENDING : OrderStatus(R.string.order_pending)
    data object CONFIRMED : OrderStatus(R.string.order_confirmed)
    data object SHIPPED : OrderStatus(R.string.order_shipped)
    data object DELIVERED : OrderStatus(R.string.order_delivered)
    data object CANCELLED : OrderStatus(R.string.order_cancelled)

    val name: String
        get() = this.javaClass.name

    companion object {
        fun values(): Array<OrderStatus> {
            return arrayOf(PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
        }

        fun valueOf(value: String): OrderStatus {
            return when (value) {
                in "PENDING", "NEW"-> PENDING
                "CONFIRMED" -> CONFIRMED
                "SHIPPED" -> SHIPPED
                "DELIVERED" -> DELIVERED
                "CANCELLED" -> CANCELLED
                else -> throw IllegalArgumentException("No object com.furniture.duet.data.model.order.OrderStatus.$value")
            }
        }
    }
}