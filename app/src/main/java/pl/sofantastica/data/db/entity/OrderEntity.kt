package pl.sofantastica.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: Int,
    val userId: String,
    val total: Double
)
