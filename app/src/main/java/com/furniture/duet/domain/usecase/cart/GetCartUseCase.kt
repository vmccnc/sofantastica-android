package com.furniture.duet.domain.usecase.cart

import android.content.Context
import com.furniture.duet.data.data_store.CountStorage
import com.furniture.duet.data.model.cart.CartItemModel
import com.furniture.duet.data.repository.CartRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cartRepo: CartRepository
) {
    suspend operator fun invoke(): List<CartItemModel> {
        val cartItem = cartRepo.getCart()
        return cartItem
    }
}
