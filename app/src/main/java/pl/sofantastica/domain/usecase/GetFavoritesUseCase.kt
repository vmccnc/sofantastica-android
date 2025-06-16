package pl.sofantastica.domain.usecase

import pl.sofantastica.data.repository.FavoritesRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke(userId: String) = repository.getFavorites(userId)
}
