package pl.sofantastica.data.repository

import kotlinx.coroutines.flow.Flow
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.CategoryDto

interface FurnitureRepository {
    fun getFurniture(): Flow<List<FurnitureDto>>
    fun getCategories(): Flow<List<CategoryDto>>
}
