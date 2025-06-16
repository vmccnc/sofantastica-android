package pl.sofantastica.data.repository

import pl.sofantastica.data.model.CartItemDto
import pl.sofantastica.data.model.CartItemUpdateDto

interface CartRepository {
    suspend fun getCart(userId: String): List<CartItemDto>
    suspend fun addItem(item: CartItemDto): Int
    suspend fun updateItem(id: Int, item: CartItemUpdateDto)
    suspend fun deleteItem(id: Int)
}
