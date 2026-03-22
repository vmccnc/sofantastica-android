package com.furniture.duet.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.domain.usecase.cart.GetCartCountUseCase
import com.furniture.duet.domain.usecase.furnitures.GetFurnitureCatalogUseCase
import com.furniture.duet.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val _getCatalog: GetFurnitureCatalogUseCase,
    private val _getCartCount: GetCartCountUseCase
) : ViewModel() {
    var cartCount by mutableStateOf(0)
        private set

    var searchQuery by mutableStateOf("")

    var searchResultList by mutableStateOf(emptyList<FurnitureCatalogModel>())
        private set

    fun getCartCount() {
        viewModelScope.launch {
            _getCartCount().collect {
                cartCount = it
            }
        }
    }

    fun onSearch() {
        viewModelScope.launch {
            searchResultList = _getCatalog(searchQuery)
        }
    }

    fun clearCartCount() {
        cartCount = 0
    }
}