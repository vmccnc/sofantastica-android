package pl.sofantastica.ui.fabricselector

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.sofantastica.data.model.FabricDto
import pl.sofantastica.domain.usecase.fabrics.GetFabricSuppliersUseCase
import pl.sofantastica.domain.usecase.fabrics.GetFabricsBySupplierUseCase
import pl.sofantastica.domain.usecase.fabrics.GetFabricsUseCase
import pl.sofantastica.domain.usecase.fabrics.GetPopularFabricsUseCase
import pl.sofantastica.domain.usecase.fabrics.LoadFabricsUseCase
import javax.inject.Inject

@HiltViewModel
class FabricSelectorViewModel @Inject constructor(
    private val getSuppliers: GetFabricSuppliersUseCase,
    private val loadFabrics: LoadFabricsUseCase,
    private val getFabricsBySupplier: GetFabricsBySupplierUseCase
) : ViewModel() {

    var fabricSuppliers by mutableStateOf<List<String>>(emptyList())
    var fabricsBySuppliers by mutableStateOf<List<List<FabricDto>>>(emptyList())

    init {
        viewModelScope.launch {
            loadFabrics()
            fabricSuppliers = getSuppliers()
            fabricsBySuppliers = fabricSuppliers.mapIndexed{ index, supplier ->
                getFabricsBySupplier(supplier)
            }
        }
    }
}
