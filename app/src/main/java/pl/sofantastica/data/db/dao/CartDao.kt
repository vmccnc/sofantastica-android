package pl.sofantastica.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pl.sofantastica.data.db.entity.CartItemEntity
import pl.sofantastica.data.model.cart.AddCartItemRequest
import pl.sofantastica.data.model.cart.CartItemModel

@Dao
interface CartDao {
    @Query("SELECT cart.id, cart.furnitureId, cart.fabricId, cart.quantity, furniture.imageUrl, " +
            "furniture.name AS furnitureName, fabrics.name AS fabricName, " +
            "(furniture.basePrice + fabrics.price)*cart.quantity AS price " +
            "FROM cart " +
            "INNER JOIN furniture ON cart.furnitureId = furniture.id " +
            "INNER JOIN fabrics ON cart.fabricId = fabrics.id")
    suspend fun getCart(): List<CartItemModel>

    @Query("SELECT * FROM cart " +
            "WHERE furnitureId = :furnitureId AND fabricId = :fabricId"
    )
    suspend fun getCartItem(furnitureId: Int, fabricId: Int): CartItemEntity?

    @Query("SELECT * FROM cart WHERE id = :id")
    suspend fun getCartItem(id: Int): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CartItemEntity)

    @Query("UPDATE cart SET quantity = :quantity WHERE id = :id")
    suspend fun setQuantity(id: Int, quantity: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CartItemEntity>)

    @Query("DELETE FROM cart WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM cart")
    suspend fun deleteAll()
}
