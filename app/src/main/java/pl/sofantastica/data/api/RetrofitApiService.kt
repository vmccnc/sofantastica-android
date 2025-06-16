package pl.sofantastica.data.api

import pl.sofantastica.data.model.FurnitureDto
import retrofit2.http.GET

interface RetrofitApiService {
    @GET("sofantastic/furniture")
    suspend fun getFurniture(): List<FurnitureDto>

    @GET("sofantastic/furniture/categories")
    suspend fun getCategories(): List<String>
}
