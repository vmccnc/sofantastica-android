package pl.sofantastica.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.sofantastica.data.db.entity.CartItemEntity
import pl.sofantastica.data.db.entity.FabricEntity
import pl.sofantastica.data.db.entity.FavoriteEntity
import pl.sofantastica.data.db.entity.FurnitureEntity
import pl.sofantastica.data.db.entity.OrderEntity
import pl.sofantastica.data.db.dao.CartDao
import pl.sofantastica.data.db.dao.FavoriteDao
import pl.sofantastica.data.db.dao.FurnitureDao
import pl.sofantastica.data.db.dao.OrderDao
import pl.sofantastica.data.db.dao.FabricDao
import pl.sofantastica.data.db.dao.FurnitureImageDao
import pl.sofantastica.data.db.entity.CategoryEntity
import pl.sofantastica.data.db.entity.FurnitureImageEntity
import pl.sofantastica.data.db.entity.SupplierEntity

@Database(
    entities = [
        FurnitureEntity::class, FurnitureImageEntity::class, FavoriteEntity::class,
        CartItemEntity::class, OrderEntity::class, FabricEntity::class,
        SupplierEntity::class, CategoryEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun furnitureDao(): FurnitureDao
    abstract fun furnitureImageDao(): FurnitureImageDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun fabricDao(): FabricDao
}
