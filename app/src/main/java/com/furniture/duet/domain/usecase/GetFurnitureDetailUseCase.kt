package com.furniture.duet.domain.usecase

import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.data.repository.FurnitureRepository
import javax.inject.Inject

class GetFurnitureDetailUseCase @Inject constructor(
    private val furnitureRepository: FurnitureRepository,
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(furnitureId: Int): FurnitureFabricDto {
//        val furniture = furnitureRepository.getFurnitureDetail(furnitureId)
//        return FurnitureFabricDto(
//            furnitureId = furnitureId,
//            fabricId = 0,
//            name = furniture.name,
//            basePrice = furniture.basePrice,
//            fabricPrice = 0.0,
//            totalPrice = furniture.basePrice,
//            description = furniture.description,
//            imageUrl = furniture.imageUrl,
//            imageUrls = furniture.imageUrls?.ifEmpty { null },
//            modelUrl = furniture.modelUrl,
//            fabricUrl = "",
//            category = furniture.category,
//            isFavorite = favoritesRepository.isFavorite(furnitureId)
//        )
        return furnitureRepository.getFurnitureDetail(furnitureId)
    }
}
