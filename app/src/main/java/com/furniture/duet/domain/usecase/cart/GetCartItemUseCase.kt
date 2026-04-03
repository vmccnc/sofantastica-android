package com.furniture.duet.domain.usecase.cart

import com.furniture.duet.data.db.entity.CartItemEntity
import com.furniture.duet.data.repository.CartRepository
import javax.inject.Inject

class GetCartItemUseCase @Inject constructor(
    private val cartRepo: CartRepository
) {
    suspend operator fun invoke(furnitureId: Int, fabricId: Int): CartItemEntity? {
        return cartRepo.getCartItem(furnitureId, fabricId)
    }
}
