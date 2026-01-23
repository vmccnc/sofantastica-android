package pl.sofantastica.domain.usecase.cart

import pl.sofantastica.R
import pl.sofantastica.data.repository.CartRepository
import pl.sofantastica.domain.exceptions.ResIdException
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(furnitureId: Int, fabricId: Int, quantity: Int) {
        val cartItem = repository.getCartItem(furnitureId, fabricId)
        if (cartItem != null) {
            if (quantity > 0) {
                repository.setQuantity(cartItem.id, quantity)
            } else {
                repository.deleteItem(cartItem.id)
            }
            return
        }
        if (fabricId == 0) {
            throw ResIdException(R.string.fabric_is_not_specified)
        }
        if (quantity == 0) {
            throw ResIdException(R.string.quantity_is_not_specified)
        }
        repository.addItem(furnitureId, fabricId, quantity)
    }
}