package com.furniture.duet.domain.usecase.fabrics

import com.furniture.duet.data.model.fabric.FabricDto
import com.furniture.duet.data.model.fabric.FabricSelectorModel
import com.furniture.duet.data.repository.FabricRepository
import javax.inject.Inject

class GetFabricsUseCase @Inject constructor(
    private val repository: FabricRepository
) {
    suspend operator fun invoke(): FabricSelectorModel {
        val map = repository.getAllFabrics()
        return FabricSelectorModel(
            suppliers = map.keys.toList(),
            fabricsBySuppliers = map.values.toList()
        )
    }
}
