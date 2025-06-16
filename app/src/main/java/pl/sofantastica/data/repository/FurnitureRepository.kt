package pl.sofantastica.data.repository

import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.CategoryDto

interface FurnitureRepository {
    suspend fun getFurniture(): List<FurnitureDto>
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getFurnitureDetail(id: Int): FurnitureDto
}
