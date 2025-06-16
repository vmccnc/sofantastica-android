package pl.sofantastica.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.repository.FurnitureRepository
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: FurnitureRepository
) : ViewModel() {

    var furniture by mutableStateOf<List<FurnitureDto>>(emptyList())
        private set

    var categories by mutableStateOf<List<CategoryDto>>(emptyList())
        private set

    var selectedCategory by mutableStateOf<CategoryDto?>(null)
        private set

    init {
        loadCategories()
        loadFurniture()
    }

    fun selectCategory(category: CategoryDto?) {
        selectedCategory = category
        loadFurniture()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categories = try {
                repository.getCategories()
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    private fun loadFurniture() {
        viewModelScope.launch {
            val list = try {
                repository.getFurniture()
            } catch (_: Exception) {
                emptyList()
            }
            furniture = selectedCategory?.let { cat ->
                list.filter { it.category == cat.name }
            } ?: list
        }
    }
}

