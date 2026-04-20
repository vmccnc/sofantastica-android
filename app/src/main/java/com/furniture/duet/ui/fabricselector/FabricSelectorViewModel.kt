package com.furniture.duet.ui.fabricselector

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.data.model.fabric.FabricDto
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.furniture.duet.data.model.fabric.FabricSelectorModel
import com.furniture.duet.domain.exceptions.ResIdException
import com.furniture.duet.domain.usecase.fabrics.GetFabricsUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class FabricSelectorViewModel @Inject constructor(
    private val getFabrics: GetFabricsUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf<UiState<FabricSelectorModel>>(UiState.Loading)
        private set

    var selectedFabric by mutableStateOf<FabricDto?>(null)
        private set

    init {
        viewModelScope.launch {
            try {
                uiState = UiState.Success(getFabrics())
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                //uiState = UiState.Error(e)
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun selectFabric(fabric: FabricDto) {
        viewModelScope.launch {
            selectedFabric = fabric
        }
    }
}
