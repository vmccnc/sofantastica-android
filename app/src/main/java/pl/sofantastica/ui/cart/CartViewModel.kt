package pl.sofantastica.ui.cart

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.sofantastica.R
import pl.sofantastica.data.model.cart.CartItemModel
import pl.sofantastica.domain.exceptions.ResIdException
import pl.sofantastica.domain.usecase.account.GetUserDataUseCase
import pl.sofantastica.domain.usecase.cart.AddToCartUseCase
import pl.sofantastica.domain.usecase.cart.ChangeQuantityUseCase
import pl.sofantastica.domain.usecase.cart.DeleteCartUseCase
import pl.sofantastica.domain.usecase.cart.GetCartUseCase
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val _getCart: GetCartUseCase,
    private val _changeQuantity: ChangeQuantityUseCase,
    private val _deleteCart: DeleteCartUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var items by mutableStateOf<List<CartItemModel>>(emptyList())
        private set

    var total by mutableDoubleStateOf(0.0)
        private set

    fun load() {
        viewModelScope.launch {
            items = try {
                _getCart()
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
                emptyList()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                emptyList()
            }
            total = items.sumOf { it.price }
        }
    }

    fun setCount(id: Int, newCount: Int) {
        viewModelScope.launch {
            try {
                if (newCount >= 1) {
                    _changeQuantity(id, newCount)
                    load()
                }
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun removeItem(id: Int) {
        viewModelScope.launch {
            try {
                _deleteCart(id)
                load()
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
