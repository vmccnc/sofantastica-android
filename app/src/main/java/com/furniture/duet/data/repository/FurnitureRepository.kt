package com.furniture.duet.data.repository

import com.furniture.duet.data.model.furniture.CategoryDto
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.model.furniture.FurnitureFabricDto

interface FurnitureRepository {
    suspend fun getMaxPrice(): Double
    suspend fun loadFurniture(): Boolean
    suspend fun getFurniture(minPrice: Double, maxPrice: Double, category: String?): List<FurnitureCatalogModel>
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getFurnitureDetail(id: Int): FurnitureFabricDto
}
