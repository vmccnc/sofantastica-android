package pl.sofantastica.data.repository

import pl.sofantastica.data.model.FabricDto

interface FabricRepository {
    suspend fun getFabrics(): List<FabricDto>
    suspend fun getFabric(id: Int): FabricDto
    suspend fun getFabricsBySupplier(supplier: String): List<FabricDto>
}
