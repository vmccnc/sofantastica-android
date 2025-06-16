package pl.sofantastica.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.sofantastica.data.api.FurnitureApi
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.CategoryDto
import javax.inject.Inject

class FurnitureRepositoryImpl @Inject constructor(
    private val api: FurnitureApi
) : FurnitureRepository {
    override fun getFurniture(): Flow<List<FurnitureDto>> = flow {
        emit(api.getFurniture())
    }

    override fun getCategories(): Flow<List<CategoryDto>> = flow {
        emit(api.getCategories())
    }
}
