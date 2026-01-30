package com.furniture.duet.ui.cart

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.furniture.duet.data.model.cart.CartModel
import com.furniture.duet.domain.usecase.cart.ChangeQuantityUseCase
import com.furniture.duet.domain.usecase.cart.DeleteCartUseCase
import com.furniture.duet.domain.usecase.cart.GetCartUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val _getCart: GetCartUseCase,
    private val _changeQuantity: ChangeQuantityUseCase,
    private val _deleteCart: DeleteCartUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf<UiState<CartModel>>(UiState.Loading)
        private set

    fun load() {
        viewModelScope.launch {
            try {
                val items = _getCart()
                uiState = UiState.Success(
                    CartModel(
                        items = items,
                        total = items.sumOf { it.price }
                    )
                )
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun setCount(id: Int, newCount: Int) {
        viewModelScope.launch {
            try {
                if (newCount >= 1) {
                    _changeQuantity(id, newCount)
                    load()
                }
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun removeItem(id: Int) {
        viewModelScope.launch {
            try {
                _deleteCart(id)
                load()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }
}
