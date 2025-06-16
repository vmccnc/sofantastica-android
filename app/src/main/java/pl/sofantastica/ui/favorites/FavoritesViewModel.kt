package pl.sofantastica.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.repository.FavoritesRepository
import pl.sofantastica.domain.usecase.GetFavoritesUseCase
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavorites: GetFavoritesUseCase,
    private val repo: FavoritesRepository
) : ViewModel() {

    var favorites: List<FurnitureDto> = emptyList()
        private set

    fun load(userId: String) {
        viewModelScope.launch {
            favorites = try {
                getFavorites(userId)
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    fun toggleFavorite(userId: String, furnitureId: Long) {
        viewModelScope.launch {
            val isFav = try { repo.isFavorite(userId, furnitureId) } catch (_: Exception) { false }
            try {
                if (isFav) repo.removeFavorite(userId, furnitureId) else repo.addFavorite(userId, furnitureId)
                load(userId)
            } catch (_: Exception) { }
        }
    }
}
