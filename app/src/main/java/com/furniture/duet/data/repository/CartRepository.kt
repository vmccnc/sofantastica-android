package com.furniture.duet.data.repository

import com.furniture.duet.data.db.entity.CartItemEntity
import com.furniture.duet.data.model.cart.CartItemModel

interface CartRepository {
    suspend fun loadCart()
    suspend fun setQuantity(id: Int, newCount: Int)
    suspend fun getCartCount(): Int
    suspend fun getCartItem(furnitureId: Int, fabricId: Int): CartItemEntity?
    suspend fun getCart(): List<CartItemModel>
    suspend fun addItem(furnitureId: Int, fabricId: Int, quantity: Int)
    suspend fun deleteItem(id: Int)
    suspend fun clear()
}
