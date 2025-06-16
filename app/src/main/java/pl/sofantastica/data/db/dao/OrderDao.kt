package pl.sofantastica.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.sofantastica.data.db.entity.OrderEntity

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE userId = :userId")
    suspend fun getOrders(userId: String): List<OrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<OrderEntity>)
}
