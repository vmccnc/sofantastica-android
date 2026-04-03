package com.furniture.duet.domain.usecase.furnitures

import com.furniture.duet.data.model.SortOption
import com.furniture.duet.data.model.furniture.CatalogModel
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.data.repository.FurnitureRepository
import javax.inject.Inject

class GetFurnitureCatalogUseCase @Inject constructor(
    private val furnitureRepository: FurnitureRepository,
    private val favoritesRepository: FavoritesRepository,
) {
    suspend operator fun invoke(
        minPrice: Float,
        maxPrice: Float,
        searchQuery: String,
        category: Int,
        sortOption: SortOption,
        page: Int
    ): CatalogModel {
        val furniturePage = furnitureRepository.getFurniture(
            minPrice.toInt(),
            maxPrice.toInt(),
            searchQuery,
            category,
            sortOption,
            page
        )
        if (furniturePage == null)
            return CatalogModel(
                list = emptyList(),
                page = 0,
                isLast = false
            )
        return CatalogModel(
            list = furniturePage.content.map {
                val isFavorite = favoritesRepository.isFavorite(it.id)
                FurnitureCatalogModel(
                    id = it.id,
                    name = it.name,
                    basePrice = it.basePrice,
                    imageUrl = it.imageUrl,
                    isFavorite = isFavorite
                )
            },
            page = furniturePage.page,
            isLast = furniturePage.isLast
        )
    }
}
