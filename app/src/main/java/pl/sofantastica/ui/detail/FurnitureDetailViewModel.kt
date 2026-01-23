package pl.sofantastica.ui.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.sofantastica.R
import pl.sofantastica.data.model.FabricDto
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.FurnitureFabricDto
import pl.sofantastica.domain.exceptions.ResIdException
import pl.sofantastica.domain.usecase.GetFurnitureDetailUseCase
import pl.sofantastica.domain.usecase.cart.AddToCartUseCase
import pl.sofantastica.domain.usecase.cart.GetQuantityInCartUseCase
import pl.sofantastica.domain.usecase.favorite.SetFavoriteUseCase
import pl.sofantastica.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class FurnitureDetailViewModel @Inject constructor(
    private val _getDetail: GetFurnitureDetailUseCase,
    private val _setFavorite: SetFavoriteUseCase,
    private val _addToCart: AddToCartUseCase,
    private val _getQuantityInCart: GetQuantityInCartUseCase,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf<UiState<FurnitureFabricDto>>(UiState.Loading)
        private set

    private var _isDialogOpened = MutableStateFlow(false)
    val isDialogOpened = _isDialogOpened.asStateFlow()

    private var _count = MutableStateFlow(0)
    val count = _count.asStateFlow()

    init {
        val furnitureId = savedStateHandle.get<Int>("furnitureId")
        if (furnitureId != null) {
            load(furnitureId)
        }
    }

    fun load(furnitureId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState = try {
                _count.update { 0 }
                UiState.Success(_getDetail(furnitureId))
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    fun openDialog() {
        viewModelScope.launch {
            _isDialogOpened.update { true }
        }
    }

    fun closeDialog() {
        viewModelScope.launch {
            _isDialogOpened.update { false }
        }
    }

    fun chooseFabric(fabric: FabricDto) {
        viewModelScope.launch {
            uiState = try {
                if (uiState is UiState.Success) {
                    val furniture = (uiState as UiState.Success).data
                    _count.update { _getQuantityInCart(furniture.furnitureId, fabric.id) }
                    UiState.Success(FurnitureFabricDto(
                        furnitureId = furniture.furnitureId,
                        fabricId = fabric.id,
                        name = furniture.name,
                        basePrice = furniture.basePrice,
                        fabricPrice = fabric.price,
                        totalPrice = furniture.basePrice + fabric.price,
                        description = furniture.description,
                        imageUrl = furniture.imageUrl,
                        fabricUrl = fabric.fabricUrl,
                        imageUrls = furniture.imageUrls,
                        modelUrl = furniture.modelUrl,
                        category = furniture.category,
                        isFavorite = furniture.isFavorite
                    ))
                } else {
                    uiState
                }
            } catch (e: Exception) {
                UiState.Error(e)
            } finally {
                _isDialogOpened.update { false }
            }

        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                val furniture = (uiState as UiState.Success).data
                _setFavorite(furniture.furnitureId, !furniture.isFavorite)
                uiState = UiState.Success(
                    FurnitureFabricDto(
                        furnitureId = furniture.furnitureId,
                        fabricId = furniture.fabricId,
                        name = furniture.name,
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

    fun setCount(newCount: Int) {
        if(newCount >= 0) {
            _count.update { newCount }
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val furniture = (uiState as UiState.Success).data
            try {
                _addToCart(
                    furnitureId = furniture.furnitureId,
                    fabricId = furniture.fabricId,
                    quantity = count.value
                )
                if (count.value > 0) {
                    Toast.makeText(context, R.string.item_added_in_cart, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.item_deleted_from_cart, Toast.LENGTH_SHORT).show()
                }
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }
}
