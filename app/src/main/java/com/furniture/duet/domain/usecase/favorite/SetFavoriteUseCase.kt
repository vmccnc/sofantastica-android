package com.furniture.duet.domain.usecase.favorite

import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.model.cart.CartItemModel
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import com.furniture.duet.data.repository.FavoritesRepository
import javax.inject.Inject

class SetFavoriteUseCase @Inject constructor(
    private val favoriteRepo: FavoritesRepository
) {
    suspend operator fun invoke(item: CartItemModel, isFavorite: Boolean) =
        if (isFavorite) {
            favoriteRepo.addFavorite(
                id = item.furnitureId,
                name = item.furnitureName,
                basePrice = item.basePrice,
                imageUrl = item.furnitureUrl
            )
        } else {
            favoriteRepo.removeFavorite(item.furnitureId)
        }

    suspend operator fun invoke(item: FurnitureCatalogModel, isFavorite: Boolean) =
        if (isFavorite) {
            favoriteRepo.addFavorite(
                id = item.id,
                name = item.name,
                basePrice = item.basePrice,
                imageUrl = item.imageUrl
            )
        } else {
            favoriteRepo.removeFavorite(item.id)
        }

    suspend operator fun invoke(item: FurnitureFabricDto, isFavorite: Boolean) =
        if (isFavorite) {
            favoriteRepo.addFavorite(
                id = item.furnitureId,
                name = item.furnitureName,
                basePrice = item.basePrice,
                imageUrl = item.imageUrl
            )
        } else {
            favoriteRepo.removeFavorite(item.furnitureId)
        }
}
