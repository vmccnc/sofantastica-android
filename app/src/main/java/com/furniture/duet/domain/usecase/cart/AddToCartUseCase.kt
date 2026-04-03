package com.furniture.duet.domain.usecase.cart

import android.content.Context
import com.furniture.duet.R
import com.furniture.duet.data.data_store.CountStorage
import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.domain.exceptions.ResIdException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cartRepo: CartRepository
) {
    suspend operator fun invoke(
        furnitureDetail: FurnitureFabricDto,
        quantity: Int
    ): Int {
        if (furnitureDetail.fabricId == 0) {
            throw ResIdException(R.string.fabric_is_not_specified)
        }
        if (quantity == 0) {
            throw ResIdException(R.string.quantity_is_not_specified)
        }
        val cartId = cartRepo.addItem(
            furnitureId = furnitureDetail.furnitureId,
            furnitureName = furnitureDetail.furnitureName,
            furnitureUrl = furnitureDetail.furnitureUrl,
            fabricId = furnitureDetail.fabricId,
            fabricName = furnitureDetail.fabricName,
            fabricUrl = furnitureDetail.fabricUrl,
            basePrice = furnitureDetail.basePrice,
            fabricPrice = furnitureDetail.fabricPrice,
            quantity = quantity
        )
        return cartId
    }
}