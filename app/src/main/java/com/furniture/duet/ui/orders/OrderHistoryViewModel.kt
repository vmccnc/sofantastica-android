package com.furniture.duet.ui.orders

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.data.model.order.OrderDto
import com.furniture.duet.domain.exceptions.ResIdException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.furniture.duet.domain.usecase.order.GetOrdersUseCase
import com.furniture.duet.ui.common.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val _getOrders: GetOrdersUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf<UiState<List<OrderDto>>>(UiState.Loading)
        private set

    fun load() {
        viewModelScope.launch {
            try {
                uiState = UiState.Success(_getOrders())
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }
}
