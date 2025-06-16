package pl.sofantastica.data.model

import pl.sofantastica.data.db.entity.CartItemEntity

data class OrderDto(
    val id: Int? = null,
    val userId: String,
    val items: List<CartItemEntity> = emptyList(),
    val total: Double = 0.0
)
