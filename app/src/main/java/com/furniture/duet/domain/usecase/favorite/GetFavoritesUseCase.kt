package com.furniture.duet.domain.usecase.favorite

import com.furniture.duet.data.repository.FavoritesRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke() = repository.getFavorites()
}
