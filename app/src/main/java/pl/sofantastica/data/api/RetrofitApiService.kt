package pl.sofantastica.data.api

import pl.sofantastica.data.model.FurnitureDto
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitApiService {
    @GET("sofantastic/furniture")
    suspend fun listFurniturs(): Response<List<FurnitureDto>>

    @GET("sofantastic/furniture/categories")
    suspend fun listCategories(): Response<List<String>>
}
