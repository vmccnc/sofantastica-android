package com.furniture.duet.domain.usecase

import com.furniture.duet.data.repository.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(userId: String) = repository.getOrders(userId)
}
