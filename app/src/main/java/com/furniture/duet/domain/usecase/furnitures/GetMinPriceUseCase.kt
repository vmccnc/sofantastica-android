package com.furniture.duet.domain.usecase.furnitures

import com.furniture.duet.data.repository.FurnitureRepository
import javax.inject.Inject

class GetMinPriceUseCase @Inject constructor(
    private val repository: FurnitureRepository
) {
    suspend operator fun invoke() = repository.getMinPrice()
}