package com.furniture.duet.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.db.dao.CartDao
import com.furniture.duet.data.db.entity.CartItemEntity
import com.furniture.duet.data.model.cart.AddCartItemRequest
import com.furniture.duet.data.model.cart.CartItemModel
import com.furniture.duet.data.model.cart.CartItemUpdateDto
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager,
    private val api: RetrofitApiService,
    private val cartDao: CartDao
) : CartRepository {
    override suspend fun loadCart() {
        connectionManager.isOnline()
        try {
            val user = auth.currentUser ?: throw IsNotAuthorizeException()
            val response = api.getCart(user.uid)
            if (response.isSuccessful) {
                val items = response.body()?.items?.map { item ->
                    CartItemEntity(
                        id = item.id,
                        furnitureId = item.furnitureId,
                        fabricId = item.fabricId,
                        quantity = item.quantity
                    )
                } ?: emptyList()

                cartDao.deleteAll()
                cartDao.insertAll(items)
            }
        } catch (_: IsNotAuthorizeException) {
        }
    }

    override suspend fun setQuantity(id: Int, newCount: Int): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        if (auth.currentUser != null) {
            if(api.updateCartItemQuantity(id, CartItemUpdateDto(newCount, 0)).isSuccessful) {
                cartDao.setQuantity(id, newCount)
            }
        } else {
            cartDao.setQuantity(id, newCount)
        }
    }

    override suspend fun getCartCount() = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        cartDao.getCartCount()
    }

    override suspend fun getCartItem(furnitureId: Int, fabricId: Int) = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        cartDao.getCartItem(furnitureId, fabricId)
    }

    override suspend fun getCart(): List<CartItemModel> = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        cartDao.getCart()
    }

    override suspend fun addItem(furnitureId: Int, fabricId: Int, quantity: Int): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        val user = auth.currentUser
        if (user == null) {
            cartDao.insert(
                CartItemEntity(
                    furnitureId = furnitureId,
                    fabricId = fabricId,
                    quantity = quantity
                )
            )
            return@withContext
        }

        val response = api.addCartItem(
            AddCartItemRequest(
                userId = user.uid,
                furnitureId = furnitureId,
                fabricId = fabricId,
                quantity = quantity
            )
        )
        if (!response.isSuccessful) {
            return@withContext
        }
        response.body()?.let {
            cartDao.insert(
                CartItemEntity(
                    id = it.id,
                    furnitureId = it.furnitureId,
                    fabricId = it.fabricId,
                    quantity = it.quantity
                )
            )
        }
    }

    override suspend fun deleteItem(id: Int): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        if (auth.currentUser != null) {
            if(api.deleteCartItem(id).isSuccessful) {
                cartDao.delete(id)
            }
        } else {
            cartDao.delete(id)
        }
    }

    override suspend fun clear(): Unit = withContext(Dispatchers.IO) {
        cartDao.deleteAll()
    }
}
