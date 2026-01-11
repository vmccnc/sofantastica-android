package pl.sofantastica.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.sofantastica.data.db.entity.FabricEntity
import pl.sofantastica.data.db.entity.SupplierEntity
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FabricDto

@Dao
interface FabricDao {
    @Query("SELECT * FROM fabrics")
    suspend fun getFabrics(): List<FabricDto>

    @Query("SELECT * FROM fabrics WHERE supplier = :supplier")
    suspend fun getFabricsBySupplier(supplier: String): List<FabricDto>

    @Query("SELECT * FROM fabrics WHERE id = :id")
    suspend fun getFabric(id: Int): FabricDto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FabricEntity>)

    @Query("DELETE FROM fabrics")
    suspend fun deleteAllFabrics()

    @Query("SELECT DISTINCT name FROM supplier")
    suspend fun getSuppliers(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuppliers(items: List<SupplierEntity>)

    @Query("DELETE FROM supplier")
    suspend fun deleteAllSuppliers()
}
