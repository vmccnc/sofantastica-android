package com.furniture.duet.domain.usecase.loading

import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.data.repository.FabricRepository
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.data.repository.FurnitureRepository
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val furnitureRepo: FurnitureRepository,
    private val fabricRepo: FabricRepository,
    private val cartRepository: CartRepository,
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke() {
        furnitureRepo.loadFurniture()
        fabricRepo.loadFabrics()
        cartRepository.loadCart()
        favoritesRepository.loadFavorites()
    }
}
