package com.furniture.duet.domain.usecase.fabrics

import com.furniture.duet.data.model.fabric.FabricDto
import com.furniture.duet.data.repository.FabricRepository
import javax.inject.Inject

class GetPopularFabricsUseCase @Inject constructor(
    private val repository: FabricRepository
) {
    suspend operator fun invoke() = emptyList<FabricDto>()//repository.getPopularFabrics()
}
