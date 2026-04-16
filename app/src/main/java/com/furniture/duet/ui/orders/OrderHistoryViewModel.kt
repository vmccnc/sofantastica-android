package com.furniture.duet.ui.orders

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.R
import com.furniture.duet.data.model.order.OrderDto
import com.furniture.duet.data.model.order.OrderHistoryModel
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

    var uiState by mutableStateOf<UiState<OrderHistoryModel>>(UiState.Loading)
        private set

    init {
        viewModelScope.launch {
            try {
                uiState = UiState.Success(_getOrders(1))
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            try {
                val data = (uiState as UiState.Success).data
                val newState = _getOrders(data.page + 1)
                uiState = UiState.Success(newState.copy(
                    orders = data.orders + newState.orders
                ))
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }
}
