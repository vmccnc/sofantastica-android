package pl.sofantastica.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.sofantastica.domain.usecase.GetOrdersUseCase
import pl.sofantastica.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrders: GetOrdersUseCase
) : ViewModel() {

    var uiState: UiState<List<pl.sofantastica.data.model.OrderDto>> = UiState.Loading
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
