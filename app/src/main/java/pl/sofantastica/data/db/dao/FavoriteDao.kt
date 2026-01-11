package pl.sofantastica.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.sofantastica.data.db.entity.FavoriteEntity
import pl.sofantastica.data.db.entity.FurnitureEntity
import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.data.model.FurnitureDto

@Dao
interface FavoriteDao {
//    @Query("SELECT * FROM favorites WHERE userId = :userId")
//    suspend fun getFavorites(userId: String): List<FavoriteEntity>
    @Query("SELECT furniture.id, furniture.name, furniture.basePrice, furniture.description, " +
            "furniture.imageUrl, furniture.category, true AS isFavorite FROM furniture " +
            "INNER JOIN favorites ON furniture.id = favorites.furnitureId")
    suspend fun getFavorites(): List<FurnitureCatalogModel>

    @Query("SELECT CASE WHEN favorites.furnitureId IS NOT NULL THEN true " +
            "ELSE false " +
            "END AS isFavorite FROM furniture " +
            "LEFT JOIN favorites ON furniture.id = favorites.furnitureId " +
            "WHERE furniture.id = :id")
    suspend fun isFavorite(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FavoriteEntity>)

    @Delete
    suspend fun delete(entity: FavoriteEntity)
}
