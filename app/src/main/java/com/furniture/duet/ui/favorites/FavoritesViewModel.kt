package com.furniture.duet.ui.favorites

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.domain.exceptions.ResIdException
import com.furniture.duet.domain.usecase.favorite.GetFavoritesUseCase
import com.furniture.duet.domain.usecase.favorite.SetFavoriteUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val _getFavorites: GetFavoritesUseCase,
    private val _setFavorite: SetFavoriteUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf<UiState<List<FurnitureCatalogModel>>>(UiState.Loading)
        private set

    fun load() {
        viewModelScope.launch {
            try {
                uiState = UiState.Success(_getFavorites())
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                //uiState = UiState.Error(e)
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun toggleFavorite(item: FurnitureCatalogModel) {
        viewModelScope.launch {
            try {
                _setFavorite(item, false)
                uiState = UiState.Success(_getFavorites())
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                //uiState = UiState.Error(e)
            }
        }
    }
}
