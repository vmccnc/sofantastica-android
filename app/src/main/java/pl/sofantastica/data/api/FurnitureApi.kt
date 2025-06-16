package pl.sofantastica.data.api

import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.CategoryDto
import retrofit2.http.GET

interface FurnitureApi {
    @GET("sofantastic/furniture")
    suspend fun getFurniture(): List<FurnitureDto>

    @GET("sofantastic/furniture/categories")
    suspend fun getCategories(): List<CategoryDto>
}
