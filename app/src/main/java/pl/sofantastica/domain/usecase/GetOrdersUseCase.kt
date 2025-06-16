package pl.sofantastica.domain.usecase

import pl.sofantastica.data.repository.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(userId: String) = repository.getOrders(userId)
}
