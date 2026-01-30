package com.furniture.duet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.furniture.duet.data.db.entity.FurnitureImageEntity

@Dao
interface FurnitureImageDao {
    @Query("SELECT imageUrl FROM furniture_image " +
            "WHERE furnitureId = :id")
    suspend fun getFurnitureImages(id: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FurnitureImageEntity>)

    @Query("DELETE FROM furniture_image")
    suspend fun deleteAll()
}