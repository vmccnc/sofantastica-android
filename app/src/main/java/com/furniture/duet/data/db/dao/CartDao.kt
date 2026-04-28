package com.furniture.duet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.furniture.duet.data.db.entity.CartItemEntity
import com.furniture.duet.data.model.cart.CartItemModel

@Dao
interface CartDao {
    @Query("SELECT cart.id, cart.furnitureId, cart.fabricId, cart.quantity, " +
            "cart.furnitureUrl AS furnitureUrl, cart.fabricUrl AS fabricUrl, " +
            "cart.furnitureName AS furnitureName, cart.fabricName AS fabricName, " +
            "cart.basePrice AS basePrice, " +
            "(cart.basePrice + cart.fabricPrice)*cart.quantity AS totalPrice, " +
            "favorites.id IS NOT NULL AS isFavorite " +
            "FROM cart " +
            "LEFT JOIN favorites ON cart.furnitureId = favorites.id ")
    suspend fun getCart(): List<CartItemModel>

    @Query("SELECT * FROM cart")
    suspend fun getLocalCart(): List<CartItemEntity>

    @Query("SELECT * FROM cart " +
            "WHERE furnitureId = :furnitureId AND fabricId = :fabricId")
    suspend fun getCartItem(furnitureId: Int, fabricId: Int): CartItemEntity?

    @Query("SELECT MAX(id) FROM cart")
    suspend fun getLastId(): Int?

    @Query("SELECT COUNT(*) FROM cart")
    fun getCartCount(): Int

    @Query("SELECT * FROM cart WHERE id = :id")
    suspend fun getCartItem(id: Int): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CartItemEntity)

    @Query("UPDATE cart SET quantity = :quantity WHERE id = :id")
    suspend fun setQuantity(id: Int, quantity: Int): Int

    @Query("UPDATE cart SET quantity = :quantity " +
            "WHERE furnitureId = :furnitureId AND fabricId = :fabricId")
    suspend fun setQuantity(furnitureId: Int, fabricId: Int, quantity: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CartItemEntity>)

    @Query("DELETE FROM cart WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM cart")
    suspend fun deleteAll()
}
