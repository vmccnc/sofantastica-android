package pl.sofantastica.domain.usecase.favorite

import pl.sofantastica.data.repository.FavoritesRepository
import javax.inject.Inject

class SetFavoriteUseCase @Inject constructor(
    private val favoriteRepo: FavoritesRepository
) {
    suspend operator fun invoke(id: Int, isFavorite: Boolean) =
        if (isFavorite) favoriteRepo.removeFavorite(id)
        else favoriteRepo.addFavorite(id)
}
