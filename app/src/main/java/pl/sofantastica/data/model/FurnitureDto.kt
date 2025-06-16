package pl.sofantastica.data.model

data class FurnitureDto(
    val id: Int,
    val name: String,
    val basePrice: Double,
    val description: String,
    val imageUrl: String,
    val imageUrls: List<String>,
    val modelUrl: String,
    val category: String
)
