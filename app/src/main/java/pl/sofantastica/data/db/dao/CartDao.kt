package pl.sofantastica.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.sofantastica.data.db.entity.CartItemEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM cart WHERE userId = :userId")
    suspend fun getCart(userId: String): List<CartItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CartItemEntity)

    @Delete
    suspend fun delete(entity: CartItemEntity)
}
