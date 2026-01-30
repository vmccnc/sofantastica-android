package com.furniture.duet.domain.usecase.cart

import com.furniture.duet.R
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.domain.exceptions.ResIdException
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepo: CartRepository
) {
    suspend operator fun invoke(furnitureId: Int, fabricId: Int, quantity: Int) {
        if (fabricId == 0) {
            throw ResIdException(R.string.fabric_is_not_specified)
        }
        if (quantity == 0) {
            throw ResIdException(R.string.quantity_is_not_specified)
        }
        val cartItem = cartRepo.getCartItem(furnitureId, fabricId)
        if (cartItem != null) {
            if (quantity > 0) {
                cartRepo.setQuantity(cartItem.id, quantity)
            } else {
                cartRepo.deleteItem(cartItem.id)
            }
        } else {
            cartRepo.addItem(furnitureId, fabricId, quantity)
        }

    }
}