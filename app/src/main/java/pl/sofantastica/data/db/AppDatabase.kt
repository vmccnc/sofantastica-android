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

@Database(
    entities = [FurnitureEntity::class, FavoriteEntity::class, CartItemEntity::class, OrderEntity::class, FabricEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun furnitureDao(): FurnitureDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun fabricDao(): FabricDao
}
