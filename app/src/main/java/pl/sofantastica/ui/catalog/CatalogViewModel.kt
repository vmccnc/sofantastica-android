package pl.sofantastica.ui.catalog

import android.content.Context
import android.widget.Toast
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
import pl.sofantastica.R
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureCatalogModel
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.repository.FurnitureRepository
import pl.sofantastica.domain.exceptions.ResIdException
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
    private val _setFavorite: SetFavoriteUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var furniture by mutableStateOf<List<FurnitureCatalogModel>>(emptyList())
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
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun selectCategory(category: CategoryDto?) {
        try {
            selectedCategory = category
            loadFurniture()
        } catch (e: ResIdException) {
            Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
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
                furniture = selectedCategory?.let { cat ->
                    list.filter { it.category == cat.name }
                } ?: list
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onSetFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                _setFavorite(id, isFavorite)
                loadFurniture()
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
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

