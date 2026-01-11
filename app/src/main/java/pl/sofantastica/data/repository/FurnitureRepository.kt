package pl.sofantastica.data.repository

import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureCatalogModel

interface FurnitureRepository {
    suspend fun getMaxPrice(): Double
    suspend fun loadFurniture(): Boolean
    suspend fun getFurniture(minPrice: Double, maxPrice: Double, category: String?): List<FurnitureCatalogModel>
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getFurnitureDetail(id: Int): FurnitureDto
}
