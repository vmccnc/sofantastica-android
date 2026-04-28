package com.furniture.duet.data.repository

import com.furniture.duet.data.db.entity.CartItemEntity
import com.furniture.duet.data.model.cart.CartItemModel

interface CartRepository {
    suspend fun loadCart()
    //suspend fun syncCart()
    suspend fun setQuantity(id: Int, newCount: Int)
    suspend fun setQuantity(furnitureId: Int, fabricId: Int, newCount: Int)
    suspend fun getCartCount(): Int
    suspend fun getCartItem(furnitureId: Int, fabricId: Int): CartItemEntity?
    suspend fun getCart(): List<CartItemModel>
    suspend fun addItem(
        furnitureId: Int,
        furnitureName: String,
        furnitureUrl: String,
        fabricId: Int,
        fabricName: String,
        fabricUrl: String,
        basePrice: Int,
        fabricPrice: Int,
        quantity: Int
    ): Int
    suspend fun deleteItem(id: Int)
    suspend fun clear()
}
