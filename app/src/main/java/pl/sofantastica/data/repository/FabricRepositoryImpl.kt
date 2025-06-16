package pl.sofantastica.data.repository

import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.model.FabricDto
import retrofit2.HttpException
import javax.inject.Inject

class FabricRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService
) : FabricRepository {
    override suspend fun getFabrics(): List<FabricDto> {
        val response = api.listFabrics()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        throw HttpException(response)
    }

    override suspend fun getFabric(id: Int): FabricDto {
        val response = api.getFabricDetail(id)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return body
        }
        throw HttpException(response)
    }

    override suspend fun getFabricsBySupplier(supplier: String): List<FabricDto> {
        val response = api.listFabricsBySupplier(supplier)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        throw HttpException(response)
    }
}
