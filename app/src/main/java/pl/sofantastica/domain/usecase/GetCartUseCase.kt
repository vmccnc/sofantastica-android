package pl.sofantastica.domain.usecase

import pl.sofantastica.data.repository.CartRepository
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(userId: String) = repository.getCart(userId)
}
