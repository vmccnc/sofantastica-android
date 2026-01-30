package com.furniture.duet.data.repository

import com.furniture.duet.data.model.fabric.FabricDto

interface FabricRepository {
    suspend fun loadFabrics(): Boolean
    suspend fun getFabric(id: Int): FabricDto?
    suspend fun getSuppliers(): List<String>
    //suspend fun getPopularFabrics(): List<FabricDto>
    suspend fun getFabricsBySupplier(supplier: String): List<FabricDto>
}
