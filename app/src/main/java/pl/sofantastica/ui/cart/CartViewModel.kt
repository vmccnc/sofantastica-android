package pl.sofantastica.ui.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.sofantastica.data.model.CartItemDto
import pl.sofantastica.data.repository.CartRepository
import pl.sofantastica.domain.usecase.GetCartUseCase
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCart: GetCartUseCase,
    private val repository: CartRepository
) : ViewModel() {

    var items by mutableStateOf<List<CartItemDto>>(emptyList())
        private set

    fun load(userId: String) {
        viewModelScope.launch {
            items = try {
                getCart(userId)
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    fun removeItem(id: Int, userId: String) {
        viewModelScope.launch {
            try {
                repository.deleteItem(id)
                load(userId)
            } catch (_: Exception) {
            }
        }
    }
}
