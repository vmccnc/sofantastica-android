package pl.sofantastica.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import pl.sofantastica.data.db.entity.CategoryEntity
import pl.sofantastica.data.db.entity.FurnitureEntity
import pl.sofantastica.data.db.entity.FurnitureImageEntity
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.FurnitureFabricDto

@Dao
interface FurnitureDao {
    @Query("SELECT id, name, basePrice, description, imageUrl, category, " +
            "CASE WHEN favorites.furnitureId IS NOT NULL THEN 1 " +
            "ELSE 0 " +
            "END AS isFavorite FROM furniture " +
            "LEFT JOIN favorites ON furniture.id = favorites.furnitureId " +
            "WHERE basePrice >= :minPrice AND basePrice <= :maxPrice")
    suspend fun getAll(minPrice: Double, maxPrice: Double): List<FurnitureCatalogModel>

    @Query("SELECT id, name, basePrice, description, imageUrl, category, " +
            "CASE WHEN favorites.furnitureId IS NOT NULL THEN 1 " +
            "ELSE 0 " +
            "END AS isFavorite FROM furniture " +
            "LEFT JOIN favorites ON furniture.id = favorites.furnitureId " +
            "WHERE basePrice >= :minPrice AND basePrice <= :maxPrice AND category = :category")
    suspend fun getAll(minPrice: Double, maxPrice: Double, category: String): List<FurnitureCatalogModel>

    @Transaction
    @Query("SELECT furniture.id AS furnitureId, 0 AS fabricId, furniture.name, " +
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

    @Query("SELECT DISTINCT name FROM categories")
    suspend fun getCategories(): List<CategoryDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(items: List<CategoryEntity>)

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("SELECT DISTINCT MAX(basePrice) FROM furniture")
    suspend fun getMax(): Double
}
