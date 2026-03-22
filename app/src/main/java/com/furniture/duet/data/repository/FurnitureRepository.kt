package com.furniture.duet.data.repository

import com.furniture.duet.data.model.SortOption
import com.furniture.duet.data.model.furniture.CategoryDto
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.model.furniture.FurnitureFabricDto

interface FurnitureRepository {
    suspend fun getMinPrice(): Float
    suspend fun getMaxPrice(): Float
    suspend fun loadFurniture(): Boolean
    suspend fun getFurniture(
        minPrice: Float,
        maxPrice: Float,
        searchQuery: String = "",
        category: String = "",
        sortOption: SortOption = SortOption.SORT_POPULAR
    ): List<FurnitureCatalogModel>
    suspend fun getFurniture(searchQuery: String): List<FurnitureCatalogModel>
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getFurnitureDetail(id: Int): FurnitureFabricDto
}
