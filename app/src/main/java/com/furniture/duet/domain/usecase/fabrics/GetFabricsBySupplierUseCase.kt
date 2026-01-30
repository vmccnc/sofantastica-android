package com.furniture.duet.domain.usecase.fabrics

import com.furniture.duet.data.repository.FabricRepository
import javax.inject.Inject

class GetFabricsBySupplierUseCase @Inject constructor(
    private val repository: FabricRepository
) {
    suspend operator fun invoke(supplier: String) = repository.getFabricsBySupplier(supplier)
}
