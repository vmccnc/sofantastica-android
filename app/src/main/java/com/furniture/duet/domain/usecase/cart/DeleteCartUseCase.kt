package com.furniture.duet.domain.usecase.cart

import com.furniture.duet.data.repository.CartRepository
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    private val cartRepo: CartRepository
) {
    suspend operator fun invoke(id: Int) {
        cartRepo.deleteItem(id)
    }
}