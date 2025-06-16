package pl.sofantastica.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fabrics")
data class FabricEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val supplier: String
)
