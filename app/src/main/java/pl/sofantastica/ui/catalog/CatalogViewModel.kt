package pl.sofantastica.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.repository.FurnitureRepository
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: FurnitureRepository
) : ViewModel() {

    private val _furniture = MutableStateFlow<List<FurnitureDto>>(emptyList())
    val furniture: StateFlow<List<FurnitureDto>> = _furniture.asStateFlow()

    private val _categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categories: StateFlow<List<CategoryDto>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<CategoryDto?>(null)
    val selectedCategory: StateFlow<CategoryDto?> = _selectedCategory.asStateFlow()

    init {
        loadCategories()
        loadFurniture()
    }

    fun selectCategory(category: CategoryDto?) {
        _selectedCategory.value = category
        loadFurniture()
    }

    private fun loadCategories() {
        repository.getCategories()
            .onEach { _categories.value = it }
            .launchIn(viewModelScope)
    }

    private fun loadFurniture() {
        repository.getFurniture()
            .onEach { list ->
                _furniture.value = _selectedCategory.value?.let { cat ->
                    list.filter { it.category == cat.name }
                } ?: list
            }
            .launchIn(viewModelScope)
    }
}
