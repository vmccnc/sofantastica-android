package pl.sofantastica.data.model

import androidx.room.Relation
import pl.sofantastica.data.db.entity.FurnitureImageEntity

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
    @Relation(parentColumn = "furnitureId", entityColumn = "furnitureId", entity = FurnitureImageEntity::class)
    val imageUrls: List<FurnitureImageEntity>?,
    val modelUrl: String,
    val category: String,
    val isFavorite: Boolean
)
