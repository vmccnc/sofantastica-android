package pl.sofantastica.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.sofantastica.domain.usecase.GetFurnitureDetailUseCase
import pl.sofantastica.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class FurnitureDetailViewModel @Inject constructor(
    private val getDetail: GetFurnitureDetailUseCase
) : ViewModel() {

    var uiState: UiState<pl.sofantastica.data.model.FurnitureDto> = UiState.Loading
        private set

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
