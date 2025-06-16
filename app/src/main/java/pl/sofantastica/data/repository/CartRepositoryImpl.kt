package pl.sofantastica.data.repository

import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.model.CartItemDto
import pl.sofantastica.data.model.CartItemUpdateDto
import retrofit2.HttpException
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService
) : CartRepository {

    override suspend fun getCart(userId: String): List<CartItemDto> {
        val response = api.getCart(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        throw HttpException(response)
    }

    override suspend fun addItem(item: CartItemDto): Int {
        val response = api.addCartItem(item)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return body.cartItemId
        }
        throw HttpException(response)
    }

    override suspend fun updateItem(id: Int, item: CartItemUpdateDto) {
        val response = api.updateCartItem(id, item)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    override suspend fun deleteItem(id: Int) {
        val response = api.deleteCartItem(id)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }
}
