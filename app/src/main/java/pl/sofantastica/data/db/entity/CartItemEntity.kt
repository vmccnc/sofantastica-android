package pl.sofantastica.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val furnitureId: Int,
    val color: String,
    val quantity: Int,
    val userId: String
)
