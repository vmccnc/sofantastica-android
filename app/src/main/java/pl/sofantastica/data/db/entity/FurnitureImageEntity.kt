package pl.sofantastica.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "furniture_image",
    primaryKeys = ["id", "imageUrl"])
data class FurnitureImageEntity(
    val id: Int,
    val imageUrl: String
)