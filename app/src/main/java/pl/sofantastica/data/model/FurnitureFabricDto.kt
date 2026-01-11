package pl.sofantastica.data.model

data class FurnitureFabricDto(
    val furnitureId: Int,
    val fabricId: Int,
    val name: String,
    val basePrice: Double,
    val fabricPrice: Double,
    val totalPrice: Double,
    val description: String,
    val imageUrl: String,
    val fabricUrl: String,
    val imageUrls: List<String>?,
    val modelUrl: String,
    val category: String,
    val isFavorite: Boolean
)
