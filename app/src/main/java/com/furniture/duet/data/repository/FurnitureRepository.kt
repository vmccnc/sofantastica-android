package com.furniture.duet.data.repository

import com.furniture.duet.data.model.SortOption
import com.furniture.duet.data.model.furniture.CategoryDto
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import com.furniture.duet.data.model.furniture.FurniturePageModel

interface FurnitureRepository {
    suspend fun getMaxPrice(): Int
    suspend fun getFurniture(
        minPrice: Int,
        maxPrice: Int,
        searchQuery: String = "",
        category: Int = 0,
        sortOption: SortOption = SortOption.SORT_POPULAR,
        page: Int
    ): FurniturePageModel?
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getFurnitureDetail(id: Int): FurnitureFabricDto
}