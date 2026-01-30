package com.furniture.duet.domain.usecase.cart

import com.furniture.duet.data.repository.CartRepository
import javax.inject.Inject

class GetQuantityInCartUseCase @Inject constructor(
    private val cartRepo: CartRepository
) {
    suspend operator fun invoke(furnitureId: Int, fabricId: Int): Int {
        return cartRepo.getCartItem(furnitureId, fabricId)?.quantity ?: 0
    }
}
