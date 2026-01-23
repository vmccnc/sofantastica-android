package pl.sofantastica.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response
import pl.sofantastica.background.InternetConnectionManager
import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.db.dao.CartDao
import pl.sofantastica.data.db.entity.CartItemEntity
import pl.sofantastica.data.model.cart.AddCartItemRequest
import pl.sofantastica.data.model.cart.CartItemDto
import pl.sofantastica.data.model.cart.CartItemModel
import pl.sofantastica.data.model.cart.CartItemUpdateDto
import pl.sofantastica.domain.exceptions.IsNotAuthorizeException
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService,
    private val dao: CartDao,
    private val auth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager
) : CartRepository {
    override suspend fun loadCart() =
        withContext(Dispatchers.IO) {
            connectionManager.isOnline()
            val user = auth.currentUser ?: throw IsNotAuthorizeException()

            val response = api.getCart(user.uid)
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    dao.deleteAll()
                    dao.insertAll(
                        responseBody.items.map { cartItemDto ->
                            CartItemEntity(
                                id = cartItemDto.id,
                                furnitureId = cartItemDto.furnitureId,
                                fabricId = cartItemDto.fabricId,
                                quantity = cartItemDto.quantity
                            )
                        }
                    )
                }
            }
        }

    override suspend fun setQuantity(id: Int, newCount: Int): Unit =
        withContext(Dispatchers.IO) {
            connectionManager.isOnline()
            auth.currentUser ?: throw IsNotAuthorizeException()
            val response = api.updateCartItemQuantity(id, CartItemUpdateDto(newCount, 0))
            if (response.isSuccessful) {
                dao.setQuantity(id, newCount)
            }
        }

    override suspend fun getCartItem(furnitureId: Int, fabricId: Int) =
        withContext(Dispatchers.IO) {
            dao.getCartItem(furnitureId, fabricId)
        }

    override suspend fun getCart(): List<CartItemModel> =
        withContext(Dispatchers.IO) {
            dao.getCart()
        }

    override suspend fun addItem(furnitureId: Int, fabricId: Int, quantity: Int) =
        withContext(Dispatchers.IO) {
            connectionManager.isOnline()
            val user = auth.currentUser ?: throw IsNotAuthorizeException()
            val response = api.addCartItem(
                AddCartItemRequest(
                    userId = user.uid,
                    furnitureId = furnitureId,
                    fabricId = fabricId,
                    quantity = quantity
                )
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    dao.insert(
                        CartItemEntity(
                            id = it.id,
                            furnitureId = furnitureId,
                            fabricId = fabricId,
                            quantity = quantity
                        )
                    )
                }
            }
        }

    override suspend fun deleteItem(id: Int) =
        withContext(Dispatchers.IO) {
            connectionManager.isOnline()
            auth.currentUser ?: throw IsNotAuthorizeException()

            if(api.deleteCartItem(id).isSuccessful) {
                dao.delete(id)
            }
        }
}
