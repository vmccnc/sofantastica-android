package pl.sofantastica.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.sofantastica.background.InternetConnectionManager
import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.db.dao.FurnitureDao
import pl.sofantastica.data.db.dao.FurnitureImageDao
import pl.sofantastica.data.db.entity.CategoryEntity
import pl.sofantastica.data.db.entity.FurnitureEntity
import pl.sofantastica.data.db.entity.FurnitureImageEntity
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureCatalogModel
import retrofit2.HttpException
import javax.inject.Inject



class FurnitureRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService,
    private val furnitureDao: FurnitureDao,
    private val furnitureImageDao: FurnitureImageDao,
    private val connectionManager: InternetConnectionManager
) : FurnitureRepository {
    override suspend fun getMaxPrice(): Double =
        withContext(Dispatchers.IO){
            furnitureDao.getMax()
        }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun loadFurniture() =
        withContext(Dispatchers.IO){
            connectionManager.isOnline()
            val responseFurniture = api.listFurniturs()

            if (responseFurniture.isSuccessful) {
                responseFurniture.body()?.let {
                    val furnitureImageList = mutableListOf<FurnitureImageEntity>()
                    val furnitureEntityList = it.stream().map { furnitureDto ->
                        furnitureDto.imageUrls?.let { imageUrls ->
                            furnitureImageList.addAll(imageUrls.stream().map { imageUrl ->
                                FurnitureImageEntity(furnitureDto.id, imageUrl)
                            }.toList())
                        }

                        FurnitureEntity(
                            id = furnitureDto.id,
                            name = furnitureDto.name,
                            category = furnitureDto.category,
                            basePrice = furnitureDto.basePrice,
                            description = furnitureDto.description,
                            imageUrl = furnitureDto.imageUrl,
                            modelUrl = furnitureDto.modelUrl
                        )
                    }.toList()
                    furnitureDao.deleteAllFurniture()
                    furnitureImageDao.deleteAll()

                    furnitureDao.insertAllFurniture(furnitureEntityList)
                    furnitureImageDao.insertAll(furnitureImageList)
                }
            }

            val responseCategories = api.listCategories()

            if (responseCategories.isSuccessful) {
                responseCategories.body()?.let {
                    val categoryEntityList = it.stream().map { categories ->
                        CategoryEntity(categories)
                    }.toList()
                    furnitureDao.deleteAllCategories()
                    furnitureDao.insertAllCategories(categoryEntityList)
                }
            }
            true
        }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun getFurniture(minPrice: Double, maxPrice: Double, category: String?): List<FurnitureCatalogModel> =
        withContext(Dispatchers.IO){
            if (category == null) {
                furnitureDao.getAll(minPrice, maxPrice).map { furnitureEntity ->
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
            } else {
                furnitureDao.getAll(minPrice, maxPrice, category).map { furnitureEntity ->
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

    override suspend fun getCategories(): List<CategoryDto> =
        withContext(Dispatchers.IO){
            furnitureDao.getCategories()
        }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun getFurnitureDetail(id: Int): FurnitureDto =
        withContext(Dispatchers.IO){
            val furnitureEntity = furnitureDao.getFurniture(id)
            FurnitureDto(
                id = furnitureEntity.id,
                name = furnitureEntity.name,
                category = furnitureEntity.category,
                basePrice = furnitureEntity.basePrice,
                description = furnitureEntity.description,
                imageUrl = furnitureEntity.imageUrl,
                modelUrl = furnitureEntity.modelUrl,
                imageUrls = furnitureImageDao.getFurnitureImages(id)
            )
        }

}

