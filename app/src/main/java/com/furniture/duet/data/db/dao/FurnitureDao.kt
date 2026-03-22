package com.furniture.duet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.furniture.duet.data.db.entity.CategoryEntity
import com.furniture.duet.data.db.entity.FurnitureEntity
import com.furniture.duet.data.model.furniture.CategoryDto
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.model.furniture.FurnitureFabricDto

@Dao
interface FurnitureDao {
    @Query("SELECT id, name, basePrice, description, imageUrl, category, " +
            "CASE WHEN favorites.furnitureId IS NOT NULL THEN 1 " +
            "ELSE 0 " +
            "END AS isFavorite FROM furniture " +
            "LEFT JOIN favorites ON furniture.id = favorites.furnitureId " +
            "WHERE basePrice >= :minPrice AND basePrice <= :maxPrice AND " +
            "name LIKE '%' || :searchQuery || '%' AND " +
            "category LIKE '%' || :category || '%' " +
            "ORDER BY basePrice ASC")
    suspend fun getAllByPriceAsc(
        minPrice: Float, maxPrice: Float,
        searchQuery: String,
        category: String
    ): List<FurnitureCatalogModel>

    @Query("SELECT id, name, basePrice, description, imageUrl, category, " +
            "CASE WHEN favorites.furnitureId IS NOT NULL THEN 1 " +
            "ELSE 0 " +
            "END AS isFavorite FROM furniture " +
            "LEFT JOIN favorites ON furniture.id = favorites.furnitureId " +
            "WHERE basePrice >= :minPrice AND basePrice <= :maxPrice AND " +
            "name LIKE '%' || :searchQuery || '%' AND " +
            "category LIKE '%' || :category || '%' " +
            "ORDER BY basePrice DESC")
    suspend fun getAllByPriceDesc(
        minPrice: Float, maxPrice: Float,
        searchQuery: String,
        category: String
    ): List<FurnitureCatalogModel>

    @Query("SELECT id, name, basePrice, description, imageUrl, category, " +
            "CASE WHEN favorites.furnitureId IS NOT NULL THEN 1 " +
            "ELSE 0 " +
            "END AS isFavorite FROM furniture " +
            "LEFT JOIN favorites ON furniture.id = favorites.furnitureId " +
            "WHERE basePrice >= :minPrice AND basePrice <= :maxPrice AND " +
            "name LIKE '%' || :searchQuery || '%' AND " +
            "category LIKE '%' || :category || '%'")
    suspend fun getAll(
        minPrice: Float, maxPrice: Float,
        searchQuery: String,
        category: String
    ): List<FurnitureCatalogModel>

    @Query("SELECT id, name, basePrice, description, imageUrl, category, " +
            "0 AS isFavorite FROM furniture " +
            "WHERE name LIKE '%' || :searchQuery || '%'")
    suspend fun getAll(searchQuery: String): List<FurnitureCatalogModel>

    @Transaction
    @Query("SELECT furniture.id AS furnitureId, 0 AS fabricId, " +
            "furniture.name AS furnitureName, \"\" AS fabricName, " +
            "furniture.basePrice, 0 AS fabricPrice, " +
            "furniture.basePrice AS totalPrice, furniture.description, " +
            "furniture.imageUrl, \"\" AS fabricUrl, furniture.modelUrl, furniture.category, " +
            "CASE WHEN favorites.furnitureId IS NOT NULL THEN 1 " +
            "ELSE 0 " +
            "END AS isFavorite " +
            "FROM furniture " +
            "LEFT JOIN favorites ON furniture.id = favorites.furnitureId " +
            "WHERE furniture.id = :id")
    suspend fun getFurniture(id: Int): FurnitureFabricDto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFurniture(items: List<FurnitureEntity>)

    @Query("DELETE FROM furniture")
    suspend fun deleteAllFurniture()

//    @Query("SELECT DISTINCT name FROM categories")
//    suspend fun getCategories(): List<CategoryDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(items: List<CategoryEntity>)

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("SELECT DISTINCT MAX(basePrice) FROM furniture")
    suspend fun getMax(): Double

    @Query("SELECT DISTINCT MIN(basePrice) FROM furniture")
    suspend fun getMin(): Double
}
