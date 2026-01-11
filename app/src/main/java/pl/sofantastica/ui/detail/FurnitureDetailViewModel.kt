package pl.sofantastica.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.sofantastica.data.model.FabricDto
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.data.model.FurnitureFabricDto
import pl.sofantastica.domain.usecase.GetFurnitureDetailUseCase
import pl.sofantastica.domain.usecase.favorite.SetFavoriteUseCase
import pl.sofantastica.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class FurnitureDetailViewModel @Inject constructor(
    private val _getDetail: GetFurnitureDetailUseCase,
    private val _setFavorite: SetFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf<UiState<FurnitureFabricDto>>(UiState.Loading)
        private set

    private var _isDialogOpened = MutableStateFlow(false)
    val isDialogOpened = _isDialogOpened.asStateFlow()

    init {
        val furnitureId = savedStateHandle.get<Int>("furnitureId")
        if (furnitureId != null) {
            load(furnitureId)
        }
    }

    fun load(furnitureId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState = try {
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
            val furniture = (uiState as UiState.Success).data
            _setFavorite(furniture.furnitureId, furniture.isFavorite)
            uiState = UiState.Success(FurnitureFabricDto(
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
            ))
        }
    }
}
