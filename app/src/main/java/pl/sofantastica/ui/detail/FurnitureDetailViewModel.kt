package pl.sofantastica.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.domain.usecase.GetFurnitureDetailUseCase
import pl.sofantastica.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class FurnitureDetailViewModel @Inject constructor(
    private val getDetail: GetFurnitureDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf<UiState<FurnitureDto>>(UiState.Loading)
        private set

    init {
        savedStateHandle.get<Int>("id")?.let { load(it) }
    }

    fun load(id: Int) {
        viewModelScope.launch {
            uiState = try {
                UiState.Success(getDetail(id))
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }
}
