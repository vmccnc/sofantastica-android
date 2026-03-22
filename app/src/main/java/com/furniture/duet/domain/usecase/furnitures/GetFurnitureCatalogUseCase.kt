package com.furniture.duet.domain.usecase.furnitures

import androidx.room.Query
import com.furniture.duet.data.model.SortOption
import com.furniture.duet.data.repository.FurnitureRepository
import javax.inject.Inject

class GetFurnitureCatalogUseCase @Inject constructor(
    private val repository: FurnitureRepository
) {
    suspend operator fun invoke(
        minPrice: Float,
        maxPrice: Float,
        searchQuery: String,
        category: String,
        sortOption: SortOption
    ) = repository.getFurniture(minPrice, maxPrice, searchQuery, category, sortOption)

    suspend operator fun invoke(searchQuery: String) = repository.getFurniture(searchQuery)
}
