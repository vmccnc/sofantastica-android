package com.furniture.duet.data.db

import androidx.room.Database
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.furniture.duet.data.db.dao.CartDao
import com.furniture.duet.data.db.dao.FavoriteDao
import com.furniture.duet.data.db.entity.CartItemEntity
import com.furniture.duet.data.db.entity.FavoriteEntity


@Database(
    entities = [FavoriteEntity::class, CartItemEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cartDao(): CartDao

    companion object {
        val migration1To2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS furniture")
                db.execSQL("DROP TABLE IF EXISTS furniture_image")
                db.execSQL("DROP TABLE IF EXISTS fabrics")
                db.execSQL("DROP TABLE IF EXISTS orders")
                db.execSQL("DROP TABLE IF EXISTS supplier")
                db.execSQL("DROP TABLE IF EXISTS categories")
                db.execSQL("DROP TABLE IF EXISTS favorites")
                db.execSQL("DROP TABLE IF EXISTS cart")

                db.execSQL("CREATE TABLE IF NOT EXISTS `favorites` (" +
                        "`id` INTEGER, " +
                        "`name` TEXT, " +
                        "`price` INTEGER, " +
                        "`imageUrl` TEXT, " +
                        "PRIMARY KEY(`id`))"
                )
                db.execSQL("CREATE TABLE IF NOT EXISTS `cart` (" +
                        "`id` INTEGER, " +
                        "`furnitureId` INTEGER, " +
                        "`furnitureName` TEXT, " +
                        "`furnitureUrl` TEXT, " +
                        "`fabricId` INTEGER, " +
                        "`fabricName` INTEGER, " +
                        "`fabricUrl` INTEGER, " +
                        "`basePrice` INTEGER, " +
                        "`fabricPrice` INTEGER, " +
                        "`quantity` INTEGER, " +
                        "PRIMARY KEY(`id`))"
                )
            }
        }
    }
}