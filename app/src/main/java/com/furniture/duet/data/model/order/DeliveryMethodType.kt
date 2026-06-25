package com.furniture.duet.data.model.order

import com.furniture.duet.R

sealed class DeliveryMethodType(val id: Int, val textId: Int, val minPrice: Int) {
    data object Pickup : DeliveryMethodType(1, R.string.delivery_pickup, 100)
    data object Standard : DeliveryMethodType(2, R.string.delivery_standard, 150)
    data object Scheduled : DeliveryMethodType(3, R.string.delivery_scheduled, 180)

    val name: String
        get() = this.javaClass.name

    companion object {
        fun values(): Array<DeliveryMethodType> {
            return arrayOf(Pickup, Standard, Scheduled)
        }

        fun valueOf(value: String): DeliveryMethodType {
            return when (value) {
                in "Pickup", "withoutDeposit" -> Pickup
                in "Standard", "withDelivery" -> Standard
                in "Scheduled", "selectedTime" -> Scheduled
                else -> throw IllegalArgumentException("No object com.furniture.duet.data.model.order.DeliveryMethodType.$value")
            }
        }
    }
}