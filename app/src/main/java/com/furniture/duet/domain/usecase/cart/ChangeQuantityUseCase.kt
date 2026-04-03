package com.furniture.duet.domain.usecase.cart

import com.furniture.duet.data.repository.CartRepository
import javax.inject.Inject

class ChangeQuantityUseCase @Inject constructor(
    private val cartRepo: CartRepository
) {
    suspend operator fun invoke(
        id: Int,
        quantity: Int
    ) {
        cartRepo.setQuantity(id, quantity)
    }

    suspend operator fun invoke(
        furnitureId: Int,
        fabricId: Int,
        quantity: Int
    ) {
        cartRepo.setQuantity(furnitureId, fabricId, quantity)
    }
}