package pl.sofantastica.data.repository

import pl.sofantastica.data.model.FabricDto

interface FabricRepository {
    suspend fun loadFabrics(): Boolean
    suspend fun getFabric(id: Int): FabricDto
    suspend fun getSuppliers(): List<String>
    suspend fun getPopularFabrics(): List<FabricDto>
    suspend fun getFabricsBySupplier(supplier: String): List<FabricDto>
}
