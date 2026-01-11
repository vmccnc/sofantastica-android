package pl.sofantastica.data.model

class FurnitureCatalogModel(
    val id: Int,
    val name: String,
    val basePrice: Double,
    val description: String,
    val imageUrl: String,
    val category: String,
    val isFavorite: Boolean
)
