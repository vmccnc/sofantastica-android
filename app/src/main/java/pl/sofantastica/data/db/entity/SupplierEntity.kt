package pl.sofantastica.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier")
data class SupplierEntity(
    @PrimaryKey val name: String
)
