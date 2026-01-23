package pl.sofantastica.domain.usecase.cart

import pl.sofantastica.data.repository.CartRepository
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteItem(id)
    }
}