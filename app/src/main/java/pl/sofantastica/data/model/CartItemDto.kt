package pl.sofantastica.data.model

data class CartItemDto(
    val id: Int? = null,
    val userId: String,
    val productId: Int,
    val quantity: Int,
    val fabricId: Int
)
