package pl.sofantastica.ui.catalog

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.repository.FurnitureRepository
import pl.sofantastica.domain.usecase.favorite.SetFavoriteUseCase
import pl.sofantastica.domain.usecase.furnitures.GetCategoriesUseCase
import pl.sofantastica.domain.usecase.furnitures.GetFurnitureCatalogUseCase
import pl.sofantastica.domain.usecase.furnitures.GetMaxPriceUseCase
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val _getMaxPrice: GetMaxPriceUseCase,
    private val _getCatalog: GetFurnitureCatalogUseCase,
    private val _getCategories: GetCategoriesUseCase,
    private val _setFavorite: SetFavoriteUseCase
) : ViewModel() {
    companion object {
        const val EMPTY_FILTERED_DATA = "There is no matching data for the selected filters"
        const val EMPTY_DATA = "It's empty here for now"
    }

    var furniture by mutableStateOf<List<FurnitureCatalogModel>>(emptyList())
        private set

    var categories by mutableStateOf<List<CategoryDto>>(emptyList())
        private set

    var selectedCategory by mutableStateOf<CategoryDto?>(null)

    var isRefreshing by mutableStateOf(false)
        private set

    var emptyMessage by mutableStateOf(EMPTY_FILTERED_DATA)
        private set

    var minPrice by mutableStateOf("0.0")
        private set

    var maxPrice by mutableStateOf("0.0")
        private set

    init {
        viewModelScope.launch {
            categories = _getCategories()
            maxPrice = _getMaxPrice().toString()
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
            val min =
                if (minPrice.isEmpty()) 0.0
                else minPrice.toDouble()
            val max =
                if (maxPrice.isEmpty()) 0.0
                else maxPrice.toDouble()
            val list = _getCatalog(min, max, selectedCategory?.name)
            furniture = selectedCategory?.let { cat ->
                list.filter { it.category == cat.name }
            } ?: list
            if (furniture.isEmpty() && list.isEmpty()) {
                emptyMessage = EMPTY_FILTERED_DATA
            } else {
                emptyMessage = ""
            }
        }
    }

    fun onSetFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            _setFavorite(id, isFavorite)
            loadFurniture()
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

