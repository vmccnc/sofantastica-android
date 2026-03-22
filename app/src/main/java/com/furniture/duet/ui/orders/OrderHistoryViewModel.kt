package com.furniture.duet.ui.orders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.data.model.order.OrderDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.furniture.duet.domain.usecase.order.GetOrdersUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val _getOrders: GetOrdersUseCase
) : ViewModel() {

    var uiState by mutableStateOf<UiState<List<OrderDto>>>(UiState.Loading)
        private set

    fun load() {
        viewModelScope.launch {
            uiState = try {
                UiState.Success(_getOrders())
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }
}
