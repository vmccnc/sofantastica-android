package pl.sofantastica.domain.usecase.cart

import pl.sofantastica.data.repository.CartRepository
import javax.inject.Inject

class GetQuantityInCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(furnitureId: Int, fabricId: Int): Int {
        val cartItem = repository.getCartItem(furnitureId, fabricId) ?: return 0
        return cartItem.quantity
    }
}
