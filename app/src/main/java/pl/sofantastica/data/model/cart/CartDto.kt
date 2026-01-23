package pl.sofantastica.data.model.cart

class CartDto(
    val items: List<CartItemDto>,
    val total: Double
)