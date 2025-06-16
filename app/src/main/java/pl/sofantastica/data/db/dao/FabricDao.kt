package pl.sofantastica.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.sofantastica.data.db.entity.FabricEntity

@Dao
interface FabricDao {
    @Query("SELECT * FROM fabrics")
    suspend fun getAll(): List<FabricEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FabricEntity>)
}
