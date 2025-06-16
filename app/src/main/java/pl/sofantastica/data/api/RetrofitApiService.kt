package pl.sofantastica.data.api

import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.FabricDto
import pl.sofantastica.data.model.OrderDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE

interface RetrofitApiService {
    @GET("sofantastic/furniture")
    suspend fun listFurniturs(): Response<List<FurnitureDto>>

    @GET("sofantastic/furniture/categories")
    suspend fun listCategories(): Response<List<String>>

    @GET("sofantastic/furniture/{id}")
    suspend fun getFurnitureDetail(@retrofit2.http.Path("id") id: Int): Response<FurnitureDto>

    @GET("sofantastic/fabric")
    suspend fun listFabrics(): Response<List<FabricDto>>

    @GET("sofantastic/fabric/{id}")
    suspend fun getFabricDetail(@retrofit2.http.Path("id") id: Int): Response<FabricDto>

    @GET("sofantastic/fabric/suppliers")
    suspend fun listSuppliers(): Response<List<String>>

    @GET("sofantastic/fabric/bySupplier/{supplier}")
    suspend fun listFabricsBySupplier(@retrofit2.http.Path("supplier") supplier: String): Response<List<FabricDto>>

    @GET("sofantastic/fabric/popular")
    suspend fun listPopularFabrics(): Response<List<FabricDto>>

    @GET("sofantastic/order/{userId}")
    suspend fun listOrders(@retrofit2.http.Path("userId") userId: String): Response<List<OrderDto>>

    @retrofit2.http.POST("sofantastic/order")
    suspend fun createOrder(@retrofit2.http.Body order: OrderDto): Response<OrderDto>

    @GET("sofantastic/favorites/{userId}")
    suspend fun listFavorites(@retrofit2.http.Path("userId") userId: String): Response<List<FurnitureDto>>

    @GET("sofantastic/favorites/{userId}/{furnitureId}")
    suspend fun isFavorite(
        @retrofit2.http.Path("userId") userId: String,
        @retrofit2.http.Path("furnitureId") furnitureId: Long
    ): Response<Boolean>

    @POST("sofantastic/favorites/{userId}/{furnitureId}")
    suspend fun addFavorite(
        @retrofit2.http.Path("userId") userId: String,
        @retrofit2.http.Path("furnitureId") furnitureId: Long
    ): Response<Unit>

    @DELETE("sofantastic/favorites/{userId}/{furnitureId}")
    suspend fun removeFavorite(
        @retrofit2.http.Path("userId") userId: String,
        @retrofit2.http.Path("furnitureId") furnitureId: Long
    ): Response<Unit>
}
