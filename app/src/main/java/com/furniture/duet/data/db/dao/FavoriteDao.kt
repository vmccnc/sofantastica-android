package com.furniture.duet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.furniture.duet.data.db.entity.FavoriteEntity
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel

@Dao
interface FavoriteDao {
    @Query("SELECT id, name, price AS basePrice, imageUrl, 1 AS isFavorite " +
            "FROM favorites")
    suspend fun getFavorites(): List<FurnitureCatalogModel>

    @Query("SELECT COUNT(*) > 0 " +
            "FROM favorites " +
            "WHERE id = :id")
    suspend fun isFavorite(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<FavoriteEntity>)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM favorites")
    suspend fun deleteAll()
}