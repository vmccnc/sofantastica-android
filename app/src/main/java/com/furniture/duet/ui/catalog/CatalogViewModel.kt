package com.furniture.duet.ui.catalog

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.furniture.duet.data.model.SortOption
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.furniture.duet.data.model.furniture.CategoryDto
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.domain.usecase.favorite.SetFavoriteUseCase
import com.furniture.duet.domain.usecase.furnitures.GetCategoriesUseCase
import com.furniture.duet.domain.usecase.furnitures.GetFurnitureCatalogUseCase
import com.furniture.duet.domain.usecase.furnitures.GetMaxPriceUseCase
import com.furniture.duet.domain.usecase.furnitures.GetMinPriceUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val _getMinPrice: GetMinPriceUseCase,
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

    var selectedCategory by mutableStateOf<String?>(null)

    var isRefreshing by mutableStateOf(false)
        private set

    var isChoosePriceRangeDialogOpened by mutableStateOf(false)
        private set

    var currentMinPrice by mutableStateOf(0f)
        private set

    var currentMaxPrice by mutableStateOf(0f)
        private set

    var minPrice by mutableStateOf(0f)
        private set

    var maxPrice by mutableStateOf(0f)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var isSortDialogOpened by mutableStateOf(false)
        private set

    var selectedSort by mutableStateOf(SortOption.SORT_POPULAR)
        private set

    init {
        viewModelScope.launch {
            try {
                categories = _getCategories()
                maxPrice = _getMaxPrice()
                currentMaxPrice = maxPrice
                minPrice = _getMinPrice()
                currentMinPrice = minPrice
                onSearch()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun selectCategory(category: String?) {
        if (category == selectedCategory) {
            selectedCategory = null
        } else {
            selectedCategory = category
        }
        onSearch()
    }

    fun refreshFurniture() {
        viewModelScope.launch {
            isRefreshing = true
            onSearch()
            isRefreshing = false
        }
    }

    fun onSetFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                _setFavorite(id, isFavorite)
                onSearch()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun openPriceRangeDialog() {
        isChoosePriceRangeDialogOpened = true
    }

    fun onSetPriceRange(min: Float, max: Float) {
        currentMinPrice = min
        currentMaxPrice = max
        isChoosePriceRangeDialogOpened = false
        onSearch()
    }

    fun onSearch() {
        viewModelScope.launch {
            isRefreshing = true
            uiState = UiState.Success(_getCatalog(
                currentMinPrice,
                currentMaxPrice,
                searchQuery,
                selectedCategory ?: "",
                selectedSort
            ))
            isRefreshing = false
        }
    }

    fun onSetSearchQuery(newSearchQuery: String) {
        searchQuery = newSearchQuery
    }

    fun openSortDialog() {
        isSortDialogOpened = true
    }

    fun onSetSortOption(newSortOption: SortOption) {
        if (newSortOption != selectedSort) {
            selectedSort = newSortOption
        }
        isSortDialogOpened = false
        onSearch()
    }
}