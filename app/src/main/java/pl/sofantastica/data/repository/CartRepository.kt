package pl.sofantastica.data.repository

import pl.sofantastica.data.db.entity.CartItemEntity
import pl.sofantastica.data.model.cart.AddCartItemRequest
import pl.sofantastica.data.model.cart.CartItemDto
import pl.sofantastica.data.model.cart.CartItemModel
import pl.sofantastica.data.model.cart.CartItemUpdateDto

interface CartRepository {
    suspend fun loadCart()
    suspend fun setQuantity(id: Int, newCount: Int)
    suspend fun getCartItem(furnitureId: Int, fabricId: Int): CartItemEntity?
    suspend fun getCart(): List<CartItemModel>
    suspend fun addItem(furnitureId: Int, fabricId: Int, quantity: Int)
    suspend fun deleteItem(id: Int)
}
