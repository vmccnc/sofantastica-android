package com.furniture.duet.domain.usecase.order

import com.furniture.duet.data.repository.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(page: Int) = repository.getOrders(page)
}
