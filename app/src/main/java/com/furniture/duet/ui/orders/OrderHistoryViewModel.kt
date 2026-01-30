package com.furniture.duet.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.data.model.OrderDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.furniture.duet.domain.usecase.GetOrdersUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrders: GetOrdersUseCase
) : ViewModel() {

    var uiState: UiState<List<OrderDto>> = UiState.Loading
        private set

    fun load(userId: String) {
        viewModelScope.launch {
            uiState = try {
                UiState.Success(getOrders(userId))
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }
}
