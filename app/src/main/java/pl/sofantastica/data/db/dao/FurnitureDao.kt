package pl.sofantastica.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.sofantastica.data.db.entity.FurnitureEntity

@Dao
interface FurnitureDao {
    @Query("SELECT * FROM furniture")
    suspend fun getAll(): List<FurnitureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FurnitureEntity>)
}
