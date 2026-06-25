package com.furniture.duet.data.repository

import android.content.Context
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.data_store.CountStorage
import com.furniture.duet.data.db.dao.CartDao
import com.furniture.duet.data.db.entity.CartItemEntity
import com.furniture.duet.data.model.cart.AddCartItemRequest
import com.furniture.duet.data.model.cart.CartItemModel
import com.furniture.duet.data.model.cart.CartItemUpdateDto
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import com.furniture.duet.domain.exceptions.IsNotFoundedException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager,
    private val api: RetrofitApiService,
    private val cartDao: CartDao
) : CartRepository {

    override suspend fun loadCart(): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        val user = auth.currentUser ?: throw IsNotAuthorizeException()
        val response = api.getCart(user.uid)
        val apiCart = response.body()!!.items
        val localCart = apiCart.map {
            val basePrice = it.checkCalculation.substringBefore(".00").toInt()
            CartItemEntity(
                id = it.id,
                furnitureId = it.furnitureId,
                furnitureName = it.furnitureName,
                furnitureUrl = it.furnitureUrl,
                fabricId = it.fabricId,
                fabricName = it.fabricName,
                fabricUrl = it.fabricUrl,
                basePrice = basePrice,
                fabricPrice = it.priceSofaWithFabric - basePrice,
                quantity = it.quantity
            )
        }
        cartDao.deleteAll()
        cartDao.insertAll(localCart)
    }

    override suspend fun syncCart(): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        val user = auth.currentUser ?: throw IsNotAuthorizeException()
        val response = api.getCart(user.uid)
        if (!response.isSuccessful || response.body() == null) return@withContext

        val apiCart = response.body()!!.items.toMutableSet()
        val localCart = cartDao.getLocalCart()

        val cartList = mutableListOf<CartItemEntity>()

        localCart.forEach { localCartItem ->
            val foundedItem = apiCart.find { apiCartItem ->
                localCartItem.furnitureId == apiCartItem.furnitureId &&
                        localCartItem.fabricId == apiCartItem.fabricId
            }
            if (foundedItem == null) {
                val addResponse = api.addCartItem(
                    AddCartItemRequest(
                        userId = user.uid,
                        furnitureId = localCartItem.furnitureId,
                        fabricId = localCartItem.fabricId,
                        quantity = localCartItem.quantity
                    )
                )
                if (addResponse.isSuccessful) {
                    addResponse.body()?.let {
                        val basePrice = it.checkCalculation.substringBefore(".00").toInt()
                        //val prices = it.checkCalculation.split(" ")
                        cartList.add(
                            CartItemEntity(
                                id = it.id,
                                furnitureId = it.furnitureId,
                                furnitureName = it.furnitureName,
                                furnitureUrl = it.furnitureUrl,
                                fabricId = it.fabricId,
                                fabricName = it.fabricName,
                                fabricUrl = it.fabricUrl,
                                basePrice = basePrice,
                                fabricPrice = it.priceSofaWithFabric - basePrice,
                                quantity = it.quantity
                            )
                        )
                    }
                }
            } else {
                val newCount = localCartItem.quantity + foundedItem.quantity
                if(api.updateCartItemQuantity(
                        foundedItem.id,
                        CartItemUpdateDto(newCount, 0)
                    ).isSuccessful) {
                    cartList.add(
                        CartItemEntity(
                            id = foundedItem.id,
                            furnitureId = foundedItem.furnitureId,
                            furnitureName = foundedItem.furnitureName,
                            furnitureUrl = foundedItem.furnitureUrl,
                            fabricId = foundedItem.fabricId,
                            fabricName = foundedItem.fabricName,
                            fabricUrl = foundedItem.fabricUrl,
                            basePrice = localCartItem.basePrice,
                            fabricPrice = localCartItem.fabricPrice,
                            quantity = newCount
                        )
                    )
                }
                apiCart.remove(foundedItem)
            }
        }
        apiCart.forEach {
            val basePrice = it.checkCalculation.substringBefore(".00").toInt()
            //val prices = it.checkCalculation.split(" ")
            cartList.add(
                CartItemEntity(
                    id = it.id,
                    furnitureId = it.furnitureId,
                    furnitureName = it.furnitureName,
                    furnitureUrl = it.furnitureUrl,
                    fabricId = it.fabricId,
                    fabricName = it.fabricName,
                    fabricUrl = it.fabricUrl,
                    basePrice = basePrice,
                    fabricPrice = it.priceSofaWithFabric - basePrice,
                    quantity = it.quantity
                )
            )
        }

        cartDao.deleteAll()
        cartDao.insertAll(cartList)
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

    override suspend fun setQuantity(furnitureId: Int, fabricId: Int, newCount: Int): Unit = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        if (auth.currentUser != null) {
            val cartItem = cartDao.getCartItem(furnitureId, fabricId) ?: throw IsNotFoundedException()
            if(api.updateCartItemQuantity(cartItem.id, CartItemUpdateDto(newCount, 0)).isSuccessful) {
                cartDao.setQuantity(cartItem.id, newCount)
            }
        } else {
            cartDao.setQuantity(furnitureId, fabricId, newCount)
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

    override suspend fun addItem(
        furnitureId: Int,
        furnitureName: String,
        furnitureUrl: String,
        fabricId: Int,
        fabricName: String,
        fabricUrl: String,
        basePrice: Int,
        fabricPrice: Int,
        quantity: Int
    ) = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        val user = auth.currentUser

        if (user == null) {
            val nextId = (cartDao.getLastId() ?: 0) + 1
            cartDao.insert(
                CartItemEntity(
                    id = nextId,
                    furnitureId = furnitureId,
                    furnitureName = furnitureName,
                    furnitureUrl = furnitureUrl,
                    fabricId = fabricId,
                    fabricName = fabricName,
                    fabricUrl = fabricUrl,
                    basePrice = basePrice,
                    fabricPrice = fabricPrice,
                    quantity = quantity
                )
            )
            return@withContext nextId
        }

        val response = api.addCartItem(
            AddCartItemRequest(
                userId = user.uid,
                furnitureId = furnitureId,
                fabricId = fabricId,
                quantity = quantity
            )
        )
        if (!response.isSuccessful) return@withContext 0
        val id = response.body()?.id ?: 0
        cartDao.insert(
            CartItemEntity(
                id = id,
                furnitureId = furnitureId,
                furnitureName = furnitureName,
                furnitureUrl = furnitureUrl,
                fabricId = fabricId,
                fabricName = fabricName,
                fabricUrl = fabricUrl,
                basePrice = basePrice,
                fabricPrice = fabricPrice,
                quantity = quantity
            )
        )
        return@withContext id
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
