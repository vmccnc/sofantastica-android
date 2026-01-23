package pl.sofantastica.data.model.cart

class AddCartItemRequest(
    var userId: String,
    val furnitureId: Int,
    val fabricId: Int,
    val quantity: Int
)
