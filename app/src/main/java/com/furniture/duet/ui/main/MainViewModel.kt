package com.furniture.duet.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.domain.usecase.cart.GetCartCountUseCase
import com.furniture.duet.domain.usecase.furnitures.GetFurnitureCatalogUseCase
import com.furniture.duet.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val _getCartCount: GetCartCountUseCase
) : ViewModel() {
    var cartCount by mutableIntStateOf(0)
        private set

    init {
        viewModelScope.launch {
            try {
            } catch (_: Exception) {

            }

            _getCartCount().collectLatest {
                cartCount = it ?: 0
            }
        }
    }

    fun clearCartCount() {
        cartCount = 0
    }
}