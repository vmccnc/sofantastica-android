package com.furniture.duet.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.db.dao.FurnitureDao
import com.furniture.duet.data.db.dao.FurnitureImageDao
import com.furniture.duet.data.db.entity.CategoryEntity
import com.furniture.duet.data.db.entity.FurnitureEntity
import com.furniture.duet.data.db.entity.FurnitureImageEntity
import com.furniture.duet.data.model.SortOption
import com.furniture.duet.data.model.furniture.CategoryDto
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import javax.inject.Inject



class FurnitureRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService,
    private val furnitureDao: FurnitureDao,
    private val furnitureImageDao: FurnitureImageDao,
    private val connectionManager: InternetConnectionManager
) : FurnitureRepository {
    override suspend fun getMinPrice(): Float = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        furnitureDao.getMin().toFloat()
    }

    override suspend fun getMaxPrice(): Float = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        furnitureDao.getMax().toFloat()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun loadFurniture() = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        val responseFurniture = api.listFurniturs()

        if (responseFurniture.isSuccessful) {
            responseFurniture.body()?.let {
                val furnitureImageList = mutableListOf<FurnitureImageEntity>()
                val furnitureEntityList = it.content.stream().map { furnitureDto ->
                    furnitureDto.imageUrls?.let { imageUrls ->
                        furnitureImageList.addAll(imageUrls.stream().map { imageUrl ->
                            FurnitureImageEntity(furnitureDto.id, imageUrl)
                        }.toList())
                    }

                    FurnitureEntity(
                        id = furnitureDto.id,
                        name = furnitureDto.name,
                        category = furnitureDto.category.title,
                        basePrice = furnitureDto.basePrice,
                        description = furnitureDto.description,
                        imageUrl = furnitureDto.imageUrl,
                        modelUrl = furnitureDto.modelUrl,
                        createdAt = furnitureDto.createdAt
                    )
                }.toList()
                furnitureDao.deleteAllFurniture()
                furnitureImageDao.deleteAll()

                furnitureDao.insertAllFurniture(furnitureEntityList)
                furnitureImageDao.insertAll(furnitureImageList)
            }
        }

//        val categories = api.listCategories()
//
//        if (responseCategories.isSuccessful) {
//            responseCategories.body()?.let {
//                val categoryEntityList = it.stream().map { categories ->
//                    CategoryEntity(categories)
//                }.toList()
//                furnitureDao.deleteAllCategories()
//                furnitureDao.insertAllCategories(categoryEntityList)
//            }
//        }
        true
    }

    override suspend fun getFurniture(
        minPrice: Float,
        maxPrice: Float,
        searchQuery: String,
        category: String,
        sortOption: SortOption
    ): List<FurnitureCatalogModel> = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        when(sortOption) {
            SortOption.SORT_BY_PRICE_ASC ->
                furnitureDao.getAllByPriceAsc(minPrice, maxPrice, searchQuery, category)
                    .map { furnitureEntity ->
                        FurnitureCatalogModel(
                            id = furnitureEntity.id,
                            name = furnitureEntity.name,
                            category = furnitureEntity.category,
                            basePrice = furnitureEntity.basePrice,
                            description = furnitureEntity.description,
                            imageUrl = furnitureEntity.imageUrl,
                            isFavorite = furnitureEntity.isFavorite
                        )
                    }
            SortOption.SORT_BY_PRICE_DESC ->
                furnitureDao.getAllByPriceDesc(minPrice, maxPrice, searchQuery, category)
                    .map { furnitureEntity ->
                        FurnitureCatalogModel(
                            id = furnitureEntity.id,
                            name = furnitureEntity.name,
                            category = furnitureEntity.category,
                            basePrice = furnitureEntity.basePrice,
                            description = furnitureEntity.description,
                            imageUrl = furnitureEntity.imageUrl,
                            isFavorite = furnitureEntity.isFavorite
                        )
                    }
            else ->
                furnitureDao.getAll(minPrice, maxPrice, searchQuery, category)
                    .map { furnitureEntity ->
                        FurnitureCatalogModel(
                            id = furnitureEntity.id,
                            name = furnitureEntity.name,
                            category = furnitureEntity.category,
                            basePrice = furnitureEntity.basePrice,
                            description = furnitureEntity.description,
                            imageUrl = furnitureEntity.imageUrl,
                            isFavorite = furnitureEntity.isFavorite
                        )
                    }
        }
    }

    override suspend fun getFurniture(searchQuery: String): List<FurnitureCatalogModel> =
        withContext(Dispatchers.IO){
            furnitureDao.getAll(searchQuery)
                .map { furnitureEntity ->
                    FurnitureCatalogModel(
                        id = furnitureEntity.id,
                        name = furnitureEntity.name,
                        category = furnitureEntity.category,
                        basePrice = furnitureEntity.basePrice,
                        description = furnitureEntity.description,
                        imageUrl = furnitureEntity.imageUrl,
                        isFavorite = furnitureEntity.isFavorite
                    )
                }
        }

    override suspend fun getCategories(): List<CategoryDto> = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        //furnitureDao.getCategories()
        api.listCategories()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun getFurnitureDetail(id: Int): FurnitureFabricDto = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        furnitureDao.getFurniture(id)
    }

}

