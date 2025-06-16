package pl.sofantastica.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "furniture")
data class FurnitureEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val category: String
)
