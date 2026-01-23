package pl.sofantastica.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val furnitureId: Int,
    val fabricId: Int,
    val quantity: Int
)
