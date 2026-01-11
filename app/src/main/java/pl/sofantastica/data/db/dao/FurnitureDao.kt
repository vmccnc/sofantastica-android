package pl.sofantastica.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.sofantastica.data.db.entity.CategoryEntity
import pl.sofantastica.data.db.entity.FurnitureEntity
import pl.sofantastica.data.db.entity.FurnitureImageEntity
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.data.model.FurnitureDto

@Dao
interface FurnitureDao {
    @Query("SELECT id, name, basePrice, description, imageUrl, category, " +
            "CASE WHEN favorites.furnitureId IS NOT NULL THEN true " +
            "ELSE false " +
            "END AS isFavorite FROM furniture " +
            "LEFT JOIN favorites ON furniture.id = favorites.furnitureId " +
            "WHERE basePrice >= :minPrice AND basePrice <= :maxPrice")
    suspend fun getAll(minPrice: Double, maxPrice: Double): List<FurnitureCatalogModel>

    @Query("SELECT id, name, basePrice, description, imageUrl, category, " +
            "CASE WHEN favorites.furnitureId IS NOT NULL THEN true " +
            "ELSE false " +
            "END AS isFavorite FROM furniture " +
            "LEFT JOIN favorites ON furniture.id = favorites.furnitureId " +
            "WHERE basePrice >= :minPrice AND basePrice <= :maxPrice AND category = :category")
    suspend fun getAll(minPrice: Double, maxPrice: Double, category: String): List<FurnitureCatalogModel>

    @Query("SELECT * FROM furniture WHERE id = :id")
    suspend fun getFurniture(id: Int): FurnitureEntity

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
