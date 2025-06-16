package pl.sofantastica.data.repository

import pl.sofantastica.data.api.RetrofitApiService
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.CategoryDto
import retrofit2.HttpException
import javax.inject.Inject



class FurnitureRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService
) : FurnitureRepository {

    override suspend fun getFurniture(): List<FurnitureDto> {
        val response = try {
            api.listFurniturs()
        } catch (e: Exception) {
            throw e
        }
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return body
        }
        throw HttpException(response)
    }

    override suspend fun getCategories(): List<CategoryDto> {
        val response = try {
            api.listCategories()
        } catch (e: Exception) {
            throw e
        }
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return body.map { CategoryDto(it) }
        }
        throw HttpException(response)
    }

    override suspend fun getFurnitureDetail(id: Int): FurnitureDto {
        val response = try {
            api.getFurnitureDetail(id)
        } catch (e: Exception) {
            throw e
        }
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return body
        }
        throw HttpException(response)
    }
}

