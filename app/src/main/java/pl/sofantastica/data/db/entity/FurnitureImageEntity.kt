package pl.sofantastica.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "furniture_image",
    primaryKeys = ["furnitureId", "imageUrl"])
data class FurnitureImageEntity(
    val furnitureId: Int,
    val imageUrl: String
)