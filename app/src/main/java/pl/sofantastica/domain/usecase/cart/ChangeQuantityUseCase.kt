package pl.sofantastica.domain.usecase.cart

import pl.sofantastica.data.repository.CartRepository
import javax.inject.Inject

class ChangeQuantityUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(id: Int, quantity: Int) {
        repository.setQuantity(id, quantity)
    }
}