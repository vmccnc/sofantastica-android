package com.furniture.duet.ui.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.furniture.duet.R
import com.furniture.duet.data.model.fabric.FabricDto
import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import com.furniture.duet.domain.exceptions.ResIdException
import com.furniture.duet.domain.usecase.furnitures.GetFurnitureDetailUseCase
import com.furniture.duet.domain.usecase.cart.AddToCartUseCase
import com.furniture.duet.domain.usecase.cart.ChangeQuantityUseCase
import com.furniture.duet.domain.usecase.cart.DeleteCartUseCase
import com.furniture.duet.domain.usecase.cart.GetCartItemUseCase
import com.furniture.duet.domain.usecase.favorite.SetFavoriteUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class FurnitureDetailViewModel @Inject constructor(
    private val _getDetail: GetFurnitureDetailUseCase,
    private val _setFavorite: SetFavoriteUseCase,
    private val _addToCart: AddToCartUseCase,
    private val _changeQuantity: ChangeQuantityUseCase,
    private val _deleteFromCart: DeleteCartUseCase,
    private val _getCartItem: GetCartItemUseCase,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf<UiState<FurnitureFabricDto>>(UiState.Loading)
        private set

    var isDialogOpened by mutableStateOf(false)
        private set

    var isDescriptionOpened by mutableStateOf(false)
        private set

    var isDimensionsOpened by mutableStateOf(false)
        private set

    var cartId by mutableIntStateOf(0)
        private set

    var count by mutableIntStateOf(0)
        private set

    init {
        val furnitureId = savedStateHandle.get<Int>("furnitureId")
        if (furnitureId != null) {
            load(furnitureId)
        }
    }

    fun load(furnitureId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            count = 0
            uiState = UiState.Success(_getDetail(furnitureId))
        }
    }

    fun openDialog() {
        viewModelScope.launch {
            isDialogOpened = true
        }
    }

    fun closeDialog() {
        viewModelScope.launch {
            isDialogOpened = false
        }
    }

    fun chooseFabric(fabric: FabricDto?) {
        viewModelScope.launch {
            try {
                if (uiState is UiState.Success && fabric != null) {
                    val furniture = (uiState as UiState.Success).data
                    val cartItem = _getCartItem(furniture.furnitureId, fabric.id)
                    uiState = UiState.Success(
                        FurnitureFabricDto(
                            furnitureId = furniture.furnitureId,
                            fabricId = fabric.id,
                            furnitureName = furniture.furnitureName,
                            furnitureUrl = furniture.furnitureUrl,
                            fabricName = fabric.name,
                            basePrice = furniture.basePrice,
                            fabricPrice = fabric.price,
                            totalPrice = furniture.basePrice + fabric.price,
                            description = furniture.description,
                            imageUrl = furniture.imageUrl,
                            fabricUrl = fabric.fabricUrl,
                            imageUrls = furniture.imageUrls,
                            modelUrl = furniture.modelUrl,
                            category = furniture.category,
                            isFavorite = furniture.isFavorite)
                    )
                    cartId = cartItem?.id ?: 0
                    count = cartItem?.quantity ?: 0
                }
                isDialogOpened = false
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                //uiState = UiState.Error(e)
            }

        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                val furniture = (uiState as UiState.Success).data
                _setFavorite(furniture, !furniture.isFavorite)
                uiState = UiState.Success(
                    FurnitureFabricDto(
                        furnitureId = furniture.furnitureId,
                        fabricId = furniture.fabricId,
                        furnitureName = furniture.furnitureName,
                        furnitureUrl = furniture.furnitureUrl,
                        fabricName = furniture.fabricName,
                        basePrice = furniture.basePrice,
                        fabricPrice = furniture.fabricPrice,
                        totalPrice = furniture.totalPrice,
                        description = furniture.description,
                        imageUrl = furniture.imageUrl,
                        fabricUrl = furniture.fabricUrl,
                        imageUrls = furniture.imageUrls,
                        modelUrl = furniture.modelUrl,
                        category = furniture.category,
                        isFavorite = !furniture.isFavorite
                    )
                )
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isFavorite(): Boolean {
        val furniture = (uiState as UiState.Success).data
        return furniture.isFavorite
    }

    fun setCountInCart(newCount: Int) {
        viewModelScope.launch {
            if (cartId == 0) return@launch
            if (newCount > 0) {
                _changeQuantity(
                    id = cartId,
                    quantity = newCount
                )
            } else {
                _deleteFromCart(cartId)
                Toast.makeText(context, R.string.item_deleted_from_cart, Toast.LENGTH_SHORT).show()
                cartId = 0
            }
            count = newCount
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val furniture = (uiState as UiState.Success).data
            try {
                count = 1
                cartId = _addToCart(
                    furnitureDetail = furniture,
                    quantity = count
                )
                Toast.makeText(context, R.string.item_added_in_cart, Toast.LENGTH_SHORT).show()
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                //uiState = UiState.Error(e)
            }
        }
    }

    fun toggleDescription() {
        viewModelScope.launch {
            isDescriptionOpened = !isDescriptionOpened
        }
    }

    fun toggleDimensions() {
        viewModelScope.launch {
            isDimensionsOpened = !isDimensionsOpened
        }
    }
}
