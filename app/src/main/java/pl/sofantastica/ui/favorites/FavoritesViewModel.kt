package pl.sofantastica.ui.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.repository.FavoritesRepository
import pl.sofantastica.domain.usecase.GetFavoritesUseCase
import pl.sofantastica.domain.usecase.favorite.SetFavoriteUseCase
import pl.sofantastica.ui.catalog.CatalogViewModel
import pl.sofantastica.ui.catalog.CatalogViewModel.Companion
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val _getFavorites: GetFavoritesUseCase,
    private val _setFavorite: SetFavoriteUseCase
) : ViewModel() {
    companion object {
        const val EMPTY_DATA = "There are no favorites yet"
    }

    var favorites by mutableStateOf<List<FurnitureCatalogModel>>(emptyList())
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    var emptyMessage by mutableStateOf(EMPTY_DATA)
        private set

    fun load() {
        viewModelScope.launch {
            favorites = _getFavorites()
            if (favorites.isEmpty()) {
                emptyMessage = EMPTY_DATA
            } else {
                emptyMessage = ""
            }
        }
    }

    fun refreshFurniture() {
        viewModelScope.launch {
            isRefreshing = true
            load()
            isRefreshing = false
        }
    }

    fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            _setFavorite(id, true)
            load()
        }
    }
}
