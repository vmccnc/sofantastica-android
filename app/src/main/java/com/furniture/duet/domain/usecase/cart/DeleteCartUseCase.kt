package com.furniture.duet.domain.usecase.cart

import android.content.Context
import com.furniture.duet.R
import com.furniture.duet.data.data_store.CountStorage
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.domain.exceptions.ResIdException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cartRepo: CartRepository
) {
    suspend operator fun invoke(id: Int) {
        cartRepo.deleteItem(id)
    }

    suspend operator fun invoke(furnitureId: Int, fabricId: Int) {
        if (fabricId == 0) {
            throw ResIdException(R.string.fabric_is_not_specified)
        }
        val cartItem = cartRepo.getCartItem(furnitureId, fabricId)
        if (cartItem != null) {
            cartRepo.deleteItem(cartItem.id)
        }
    }
}