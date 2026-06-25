package com.furniture.duet.data.model.order

data class OrderModel(
    val id: Int? = null,
    val userId: String,
    val customerType: CustomerType,
    val firstAndLastName: String,
    val companyName: String,
    val unn: String,
    val email: String,
    val phone: String,
    val address: String,
    val city: String,
    val postCode: String,
    val country: String,
    val typeOfDelivery: DeliveryMethodType,
    val deliveryMethodId: Int?,
    val deliveryTime: String,
    val typeOfPayment: PaymentType,
    val orderDate: String,
    val status: OrderStatus,
    val deliveryCost: Int,
    val amount: Int,
    val items: List<OrderItemDto>
)