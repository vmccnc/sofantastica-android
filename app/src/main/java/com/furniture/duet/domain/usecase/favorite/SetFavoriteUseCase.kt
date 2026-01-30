package com.furniture.duet.domain.usecase.favorite

import com.furniture.duet.data.repository.FavoritesRepository
import javax.inject.Inject

class SetFavoriteUseCase @Inject constructor(
    private val favoriteRepo: FavoritesRepository
) {
    suspend operator fun invoke(id: Int, isFavorite: Boolean) =
        if (isFavorite) favoriteRepo.addFavorite(id)
        else favoriteRepo.removeFavorite(id)
}
