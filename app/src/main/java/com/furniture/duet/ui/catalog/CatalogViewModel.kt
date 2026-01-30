package com.furniture.duet.ui.catalog

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.furniture.duet.data.model.furniture.CategoryDto
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.domain.usecase.favorite.SetFavoriteUseCase
import com.furniture.duet.domain.usecase.furnitures.GetCategoriesUseCase
import com.furniture.duet.domain.usecase.furnitures.GetFurnitureCatalogUseCase
import com.furniture.duet.domain.usecase.furnitures.GetMaxPriceUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val _getMaxPrice: GetMaxPriceUseCase,
    private val _getCatalog: GetFurnitureCatalogUseCase,
    private val _getCategories: GetCategoriesUseCase,
    private val _setFavorite: SetFavoriteUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf<UiState<List<FurnitureCatalogModel>>>(UiState.Loading)
        private set

    var categories by mutableStateOf<List<CategoryDto>>(emptyList())
        private set

    var selectedCategory by mutableStateOf<CategoryDto?>(null)

    var isRefreshing by mutableStateOf(false)
        private set

    var minPrice by mutableStateOf("0.00")
        private set

    var maxPrice by mutableStateOf("0.00")
        private set

    init {
        viewModelScope.launch {
            try {
                categories = _getCategories()
                maxPrice = _getMaxPrice().toString()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun selectCategory(category: CategoryDto?) {
        selectedCategory = category
        loadFurniture()
    }

    fun refreshFurniture() {
        viewModelScope.launch {
            isRefreshing = true
            loadFurniture()
            isRefreshing = false
        }
    }

    fun loadFurniture() {
        viewModelScope.launch {
            try {
                val min =
                    if (minPrice.isEmpty()) 0.0
                    else minPrice.toDouble()
                val max =
                    if (maxPrice.isEmpty()) 0.0
                    else maxPrice.toDouble()
                val list = _getCatalog(min, max, selectedCategory?.name)
                uiState = UiState.Success(
                    selectedCategory?.let { cat ->
                        list.filter { it.category == cat.name }
                    } ?: list
                )
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun onSetFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                _setFavorite(id, isFavorite)
                loadFurniture()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun onSetMinPrice(newPriceStr: String) {
        val newPrice = newPriceStr.toDoubleOrNull() ?: -1.0
        if (newPriceStr.isEmpty() || newPrice >= 0.0) {
            minPrice = newPriceStr
        }
        loadFurniture()
    }

    fun onSetMaxPrice(newPriceStr: String) {
        val newPrice = newPriceStr.toDoubleOrNull() ?: -1.0
        if (newPriceStr.isEmpty() || newPrice >= 0.0) {
            maxPrice = newPriceStr
        }
        loadFurniture()
    }
}

