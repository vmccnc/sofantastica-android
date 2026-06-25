package com.furniture.duet.data.repository

import androidx.compose.ui.util.fastCbrt
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.db.dao.CartDao
import com.furniture.duet.data.db.dao.FavoriteDao
import com.furniture.duet.data.db.entity.FavoriteEntity
import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import com.furniture.duet.domain.exceptions.IsNotFoundedException
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val api: RetrofitApiService,
    private val favoriteDao: FavoriteDao,
    private val connectionManager: InternetConnectionManager
) : FavoritesRepository {
    override suspend fun loadFavorites() {
        connectionManager.isOnline()
        try {
            val user = auth.currentUser ?: throw IsNotAuthorizeException()
            val response = api.listFavorites(user.uid)
            val items = if (response.isSuccessful) {
                response.body()?.map { favoriteItem ->
                    FavoriteEntity(
                        id = favoriteItem.id,
                        name = favoriteItem.name,
                        price = favoriteItem.basePrice,
                        imageUrl = favoriteItem.imageUrl
                    )
                } ?: emptyList()
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

    override suspend fun addFavorite(
        id: Int,
        name: String,
        basePrice: Int,
        imageUrl: String
    ): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        val user = auth.currentUser

        if (user != null) {
            val response = api.addFavorite(user.uid, id.toLong())
            if(!response.isSuccessful) throw IsNotFoundedException()
        }

        favoriteDao.insert(FavoriteEntity(
            id = id,
            name = name,
            price = basePrice,
            imageUrl = imageUrl
        ))
    }

    override suspend fun removeFavorite(furnitureId: Int): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        val user = auth.currentUser

        if (user != null) {
            val response = api.removeFavorite(user.uid, furnitureId.toLong())
            if(!response.isSuccessful) return@withContext
        }
        favoriteDao.delete(furnitureId)
    }

    override suspend fun isFavorite(furnitureId: Int) = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        favoriteDao.isFavorite(furnitureId)
    }

    override suspend fun clear(): Unit = withContext(Dispatchers.IO) {
        favoriteDao.deleteAll()
    }
}
