package pl.sofantastica.data.model.cart

data class CartItemDto(
    val id: Int,
    val userId: String,
    val furnitureId: Int,
    val furnitureName: String,
    val furnitureUrl: String,
    val fabricId: Int,
    val fabricName: String,
    val fabricUrl: String,
    val priceSofaWithFabric: Double,
    val quantity: Int,
    val checkCalculation: String,
    val priceForQuantity: Double
)
