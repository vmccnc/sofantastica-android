package com.furniture.duet.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.db.dao.FavoriteDao
import com.furniture.duet.data.model.SortOption
import com.furniture.duet.data.model.furniture.CategoryDto
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import com.furniture.duet.data.model.furniture.FurniturePageModel
import com.furniture.duet.domain.exceptions.IsNotFoundedException
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject



class FurnitureRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService,
    private val favoriteDao: FavoriteDao,
    private val connectionManager: InternetConnectionManager
) : FurnitureRepository {

    companion object {
        private var maxPrice: Int = 0
    }

    override suspend fun getMaxPrice() = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        maxPrice = api.getMaxPrice().body() ?: 0
        maxPrice
    }

    override suspend fun getFurniture(
        minPrice: Int,
        maxPrice: Int,
        searchQuery: String,
        category: Int,
        sortOption: SortOption,
        page: Int
    ): FurniturePageModel? = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        val response =
            if(category == 1) {
                api.listFurniture(
                    minPrice, maxPrice,
                    sortOption.sortBy, sortOption.sortOrder,
                    searchQuery,
                    page, 6
                )
            } else {
                api.listFurniture(
                    category,
                    minPrice, maxPrice,
                    sortOption.sortBy, sortOption.sortOrder,
                    searchQuery,
                    page, 6
                )
            }

        if (!response.isSuccessful) null
        response.body()
    }

    override suspend fun getCategories(): List<CategoryDto> = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        api.listCategories()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun getFurnitureDetail(id: Int) = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        val response = api.getFurnitureDetail(id)
        if (!response.isSuccessful || response.body() == null) throw IsNotFoundedException()
        val furnitureDetail = response.body()!!

        FurnitureFabricDto(
            furnitureId = furnitureDetail.id,
            fabricId = 0,
            furnitureName = furnitureDetail.name,
            furnitureUrl = furnitureDetail.imageUrl,
            fabricName = "",
            basePrice = furnitureDetail.basePrice,
            fabricPrice = 0,
            totalPrice = furnitureDetail.basePrice,
            description = furnitureDetail.description,
            imageUrl = furnitureDetail.imageUrl,
            fabricUrl = "",
            imageUrls = furnitureDetail.imageUrls,
            modelUrl = furnitureDetail.modelUrl,
            category = furnitureDetail.category.title,
            isFavorite = favoriteDao.isFavorite(furnitureDetail.id)
        )
    }

}

