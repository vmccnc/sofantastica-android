package com.furniture.duet.domain.usecase.furnitures

import com.furniture.duet.data.repository.FurnitureRepository
import javax.inject.Inject

class GetFurnitureCatalogUseCase @Inject constructor(
    private val repository: FurnitureRepository
) {
    suspend operator fun invoke(minPrice: Double, maxPrice: Double, category: String?) =
        repository.getFurniture(minPrice, maxPrice, category)
}
