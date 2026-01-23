package pl.sofantastica.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.sofantastica.background.InternetConnectionManager
import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.db.dao.FavoriteDao
import pl.sofantastica.data.db.dao.FurnitureDao
import pl.sofantastica.data.db.dao.FurnitureImageDao
import pl.sofantastica.data.db.entity.FavoriteEntity
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.FurnitureFabricDto
import pl.sofantastica.domain.exceptions.IsNotAuthorizeException
import retrofit2.HttpException
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService,
    private val favoriteDao: FavoriteDao,
    private val furnitureDao: FurnitureDao,
    private val furnitureImageDao: FurnitureImageDao,
    private val auth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager
) : FavoritesRepository {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun loadFavorite() = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        auth.currentUser ?: throw IsNotAuthorizeException()
        val response = api.listFavorites(auth.currentUser!!.uid)
        if (response.isSuccessful) {
            response.body()?.let { furnitureList ->
                favoriteDao.insertAll(furnitureList.stream().map {
                        furniture -> FavoriteEntity(furniture.id)
                }.toList())
            }
        }
        true
    }

    override suspend fun getFavorites() = withContext(Dispatchers.IO) {
        favoriteDao.getFavorites()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun getFavoriteFurnitureDetail(id: Int) =
        withContext(Dispatchers.IO){
            furnitureDao.getFurniture(id)
        }

    override suspend fun addFavorite(furnitureId: Int) = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        auth.currentUser ?: throw IsNotAuthorizeException()
        val response = api.addFavorite(auth.currentUser!!.uid, furnitureId.toLong())
        if (response.isSuccessful) {
            favoriteDao.insert(FavoriteEntity(furnitureId))
            true
        } else {
            false
        }
    }

    override suspend fun removeFavorite(furnitureId: Int) = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        auth.currentUser ?: throw IsNotAuthorizeException()
        val response = api.removeFavorite(auth.currentUser!!.uid, furnitureId.toLong())
        if (response.isSuccessful) {
            favoriteDao.delete(FavoriteEntity(furnitureId))
            true
        } else {
            false
        }
    }

    override suspend fun isFavorite(furnitureId: Int) = withContext(Dispatchers.IO) {
        favoriteDao.isFavorite(furnitureId)
    }
}
