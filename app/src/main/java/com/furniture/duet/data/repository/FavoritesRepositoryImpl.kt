package com.furniture.duet.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.db.dao.FavoriteDao
import com.furniture.duet.data.db.dao.FurnitureDao
import com.furniture.duet.data.db.entity.FavoriteEntity
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService,
    private val favoriteDao: FavoriteDao,
    private val furnitureDao: FurnitureDao,
    private val auth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager
) : FavoritesRepository {
    override suspend fun loadFavorites() {
        connectionManager.isOnline()
        try {
            val user = auth.currentUser ?: throw IsNotAuthorizeException()
            val response = api.listFavorites(user.uid)
            val items = if (response.isSuccessful) {
                response.body()?.map { favoriteItem -> FavoriteEntity(favoriteItem.id) } ?: emptyList()
            } else {
                emptyList()
            }
            favoriteDao.deleteAll()
            favoriteDao.insertAll(items)
        } catch (_: IsNotAuthorizeException) {
        }
    }

    override suspend fun getFavorites() = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        favoriteDao.getFavorites()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun getFavoriteFurnitureDetail(id: Int) = withContext(Dispatchers.IO){
        connectionManager.isOnline()
        furnitureDao.getFurniture(id)
    }

    override suspend fun addFavorite(furnitureId: Int): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        val user = auth.currentUser

        if (user != null) {
            val response = api.addFavorite(user.uid, furnitureId.toLong())
            if(!response.isSuccessful) return@withContext
        }
        favoriteDao.insert(FavoriteEntity(furnitureId))
    }

    override suspend fun removeFavorite(furnitureId: Int): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        val user = auth.currentUser

        if (user != null) {
            val response = api.removeFavorite(user.uid, furnitureId.toLong())
            if(!response.isSuccessful) return@withContext
        }
        favoriteDao.delete(FavoriteEntity(furnitureId))
    }

    override suspend fun isFavorite(furnitureId: Int) = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        favoriteDao.isFavorite(furnitureId)
    }

    override suspend fun clear(): Unit = withContext(Dispatchers.IO) {
        favoriteDao.deleteAll()
    }
}
