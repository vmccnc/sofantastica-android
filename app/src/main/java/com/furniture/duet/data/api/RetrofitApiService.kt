package com.furniture.duet.data.api

import com.furniture.duet.data.model.furniture.FurnitureDto
import com.furniture.duet.data.model.fabric.FabricDto
import com.furniture.duet.data.model.order.OrderDto
import com.furniture.duet.data.model.PriceDto
import com.furniture.duet.data.model.account.AccountModel
import com.furniture.duet.data.model.cart.CartItemDto
import com.furniture.duet.data.model.cart.AddCartItemResponse
import com.furniture.duet.data.model.cart.CartItemUpdateDto
import com.furniture.duet.data.model.cart.AddCartItemRequest
import com.furniture.duet.data.model.cart.CartDto
import com.furniture.duet.data.model.cart.CartItemUpdateResponse
import com.furniture.duet.data.model.furniture.CategoryDto
import com.furniture.duet.data.model.furniture.FurniturePageModel
import com.furniture.duet.data.model.order.CreateOrderModel
import com.furniture.duet.data.model.order.OrderResponseDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.PUT

interface RetrofitApiService {
    @GET("sofantastic/furniture")
    suspend fun listFurniture(
        @retrofit2.http.Query("categoryId") category: Int,
        @retrofit2.http.Query("minPrice") min: Int,
        @retrofit2.http.Query("maxPrice") max: Int,
        @retrofit2.http.Query("sortBy") sortBy: String,
        @retrofit2.http.Query("sortOrder") sortOrder: String,
        @retrofit2.http.Query("keyword") keyword: String,
        @retrofit2.http.Query("page") page: Int,
        @retrofit2.http.Query("size") size: Int
    ): Response<FurniturePageModel>
    @GET("sofantastic/furniture")
    suspend fun listFurniture(
        @retrofit2.http.Query("minPrice") min: Int,
        @retrofit2.http.Query("maxPrice") max: Int,
        @retrofit2.http.Query("sortBy") sortBy: String,
        @retrofit2.http.Query("sortOrder") sortOrder: String,
        @retrofit2.http.Query("keyword") keyword: String,
        @retrofit2.http.Query("page") page: Int,
        @retrofit2.http.Query("size") size: Int
    ): Response<FurniturePageModel>

    @GET("sofantastic/categories")
    suspend fun listCategories(): List<CategoryDto>

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

    @GET("sofantastic/furniture/maxPrice")
    suspend fun getMaxPrice(): Response<Int>

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
    ): Response<CartItemUpdateResponse>

    @DELETE("sofantastic/cart/{id}")
    suspend fun deleteCartItem(
        @retrofit2.http.Path("id") id: Int
    ): Response<Unit>

    @GET("sofantastic/order/{userId}")
    suspend fun listOrders(
        @retrofit2.http.Path("userId") userId: String,
        @retrofit2.http.Query("page") page: Int,
        @retrofit2.http.Query("size") size: Int
    ): Response<OrderResponseDto>

    @retrofit2.http.POST("sofantastic/order/place")
    suspend fun createOrder(@retrofit2.http.Body order: CreateOrderModel): Response<OrderDto>

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

    @GET("sofantastic/user/{userId}")
    suspend fun getUser(@retrofit2.http.Path("userId") userId: String): Response<AccountModel>

    @POST("sofantastic/user")
    suspend fun createUser(
        @retrofit2.http.Body user: AccountModel
    ): Response<AccountModel>

    @PUT("sofantastic/user/{id}")
    suspend fun updateUser(
        @retrofit2.http.Path("id") id: String,
        @retrofit2.http.Body user: AccountModel
    ): Response<AccountModel>

}
