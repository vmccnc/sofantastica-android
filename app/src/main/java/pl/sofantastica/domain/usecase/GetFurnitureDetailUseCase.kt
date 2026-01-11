package pl.sofantastica.domain.usecase

import pl.sofantastica.data.db.dao.FavoriteDao
import pl.sofantastica.data.model.FurnitureFabricDto
import pl.sofantastica.data.repository.FabricRepository
import pl.sofantastica.data.repository.FavoritesRepository
import pl.sofantastica.data.repository.FurnitureRepository
import javax.inject.Inject

class GetFurnitureDetailUseCase @Inject constructor(
    private val furnitureRepository: FurnitureRepository,
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(furnitureId: Int): FurnitureFabricDto {
        val furniture = furnitureRepository.getFurnitureDetail(furnitureId)
        return FurnitureFabricDto(
            furnitureId = furnitureId,
            fabricId = 0,
            name = furniture.name,
            basePrice = furniture.basePrice,
            fabricPrice = 0.0,
            totalPrice = furniture.basePrice,
            description = furniture.description,
            imageUrl = furniture.imageUrl,
            imageUrls = furniture.imageUrls?.ifEmpty { null },
            modelUrl = furniture.modelUrl,
            fabricUrl = "",
            category = furniture.category,
            isFavorite = favoritesRepository.isFavorite(furnitureId)
        )
    }
}
