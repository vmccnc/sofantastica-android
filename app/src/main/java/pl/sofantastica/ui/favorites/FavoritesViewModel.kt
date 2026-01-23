package pl.sofantastica.ui.favorites

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.domain.exceptions.ResIdException
import pl.sofantastica.domain.usecase.GetFavoritesUseCase
import pl.sofantastica.domain.usecase.favorite.SetFavoriteUseCase
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val _getFavorites: GetFavoritesUseCase,
    private val _setFavorite: SetFavoriteUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var favorites by mutableStateOf<List<FurnitureCatalogModel>>(emptyList())
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    fun load() {
        viewModelScope.launch {
            try {
                favorites = _getFavorites()
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
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
            try {
                _setFavorite(id, false)
                favorites = _getFavorites()
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
