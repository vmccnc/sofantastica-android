package com.furniture.duet.data.api

import com.furniture.duet.data.model.furniture.FurnitureDto
import com.furniture.duet.data.model.fabric.FabricDto
import com.furniture.duet.data.model.OrderDto
import com.furniture.duet.data.model.PriceDto
import com.furniture.duet.data.model.cart.CartItemDto
import com.furniture.duet.data.model.cart.AddCartItemResponse
import com.furniture.duet.data.model.cart.CartItemUpdateDto
import com.furniture.duet.data.model.cart.AddCartItemRequest
import com.furniture.duet.data.model.cart.CartDto
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

    @GET("sofantastic/price")
    suspend fun getPrice(
        @retrofit2.http.Query("furnitureId") furnitureId: Int,
        @retrofit2.http.Query("fabricId") fabricId: Int
    ): Response<PriceDto>

    @GET("sofantastic/cart/{userId}")
    suspend fun getCart(
        @retrofit2.http.Path("userId") userId: String
    ): Response<CartDto>//Response<List<CartItemDto>>

    @POST("sofantastic/cart")
    suspend fun addCartItem(
        @retrofit2.http.Body item: AddCartItemRequest
    ): Response<AddCartItemResponse>

    @retrofit2.http.PATCH("sofantastic/cart/{cartItemId}/color_and_quantity")
    suspend fun updateCartItem(
        @retrofit2.http.Path("cartItemId") cartItemId: Int,
        @retrofit2.http.Body item: CartItemUpdateDto
    ): Response<CartItemDto>

    @retrofit2.http.PATCH("sofantastic/cart/{cartItemId}/quantity")
    suspend fun updateCartItemQuantity(
        @retrofit2.http.Path("cartItemId") cartItemId: Int,
        @retrofit2.http.Body item: CartItemUpdateDto
    ): Response<CartItemDto>

    @DELETE("sofantastic/cart/{id}")
    suspend fun deleteCartItem(
        @retrofit2.http.Path("id") id: Int
    ): Response<Unit>

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
