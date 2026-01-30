package com.furniture.duet.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.furniture.duet.data.db.entity.CartItemEntity
import com.furniture.duet.data.db.entity.FabricEntity
import com.furniture.duet.data.db.entity.FavoriteEntity
import com.furniture.duet.data.db.entity.FurnitureEntity
import com.furniture.duet.data.db.entity.OrderEntity
import com.furniture.duet.data.db.dao.CartDao
import com.furniture.duet.data.db.dao.FavoriteDao
import com.furniture.duet.data.db.dao.FurnitureDao
import com.furniture.duet.data.db.dao.OrderDao
import com.furniture.duet.data.db.dao.FabricDao
import com.furniture.duet.data.db.dao.FurnitureImageDao
import com.furniture.duet.data.db.entity.CategoryEntity
import com.furniture.duet.data.db.entity.FurnitureImageEntity
import com.furniture.duet.data.db.entity.SupplierEntity

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
