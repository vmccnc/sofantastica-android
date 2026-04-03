package com.furniture.duet.ui.catalog

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.furniture.duet.data.model.SortOption
import com.furniture.duet.data.model.furniture.CatalogModel
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

    var uiState by mutableStateOf<UiState<CatalogModel>>(UiState.Loading)
        private set

    var categories by mutableStateOf<List<CategoryDto>>(emptyList())
        private set

    var selectedCategory by mutableIntStateOf(1)

    var isChoosePriceRangeDialogOpened by mutableStateOf(false)
        private set

    var currentMinPrice by mutableFloatStateOf(0f)
        private set

    var currentMaxPrice by mutableFloatStateOf(0f)
        private set

    var minPrice by mutableFloatStateOf(0f)
        private set

    var maxPrice by mutableFloatStateOf(0f)
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
                maxPrice = _getMaxPrice().toFloat()
                currentMaxPrice = maxPrice
                minPrice = 0f
                currentMinPrice = minPrice
                onSearch()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun selectCategory(category: Int) {
        if (category == selectedCategory) {
            selectedCategory = 1
        } else {
            selectedCategory = category
        }
        onSearch()
    }

    fun onToggleFavorite(item: FurnitureCatalogModel) {
        viewModelScope.launch {
            try {
                _setFavorite(item, !item.isFavorite)

                val data = (uiState as UiState.Success).data
                uiState = UiState.Success(CatalogModel(
                    list = data.list.map {
                        if (it == item) {
                            FurnitureCatalogModel(
                                id = it.id,
                                name = it.name,
                                basePrice = it.basePrice,
                                imageUrl = it.imageUrl,
                                isFavorite = !it.isFavorite
                            )
                        } else {
                            it
                        }
                    },
                    page = data.page,
                    isLast = data.isLast
                ))

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
            uiState = UiState.Success(_getCatalog(
                currentMinPrice,
                currentMaxPrice,
                searchQuery,
                selectedCategory,
                selectedSort,
                0
            ))
        }
    }

    fun onSetSearchQuery(newSearchQuery: String) {
        searchQuery = newSearchQuery
        onSearch()
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

    fun loadNextPage() {
        viewModelScope.launch {
            val data = (uiState as UiState.Success).data
            val newState = _getCatalog(
                currentMinPrice,
                currentMaxPrice,
                searchQuery,
                selectedCategory,
                selectedSort,
                data.page + 1
            )
            uiState = UiState.Success(newState.copy(
                list = data.list + newState.list
            ))
        }
    }
}