package com.furniture.duet.ui.fabricselector

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.furniture.duet.data.model.fabric.FabricSelectorModel
import com.furniture.duet.domain.exceptions.ResIdException
import com.furniture.duet.domain.usecase.fabrics.GetFabricSuppliersUseCase
import com.furniture.duet.domain.usecase.fabrics.GetFabricsBySupplierUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class FabricSelectorViewModel @Inject constructor(
    private val getSuppliers: GetFabricSuppliersUseCase,
    private val getFabricsBySupplier: GetFabricsBySupplierUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf<UiState<FabricSelectorModel>>(UiState.Loading)
        private set

    init {
        viewModelScope.launch {
            try {
                val fabricSuppliers = getSuppliers()
                uiState = UiState.Success(
                    FabricSelectorModel(
                        fabricSuppliers = fabricSuppliers,
                        fabricsBySuppliers = fabricSuppliers.map{ supplier ->
                            getFabricsBySupplier(supplier)
                        }
                    )
                )

            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }

        }
    }
}
