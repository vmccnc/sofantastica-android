package pl.sofantastica.ui.fabricselector

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
import pl.sofantastica.data.model.FabricDto
import pl.sofantastica.domain.exceptions.ResIdException
import pl.sofantastica.domain.usecase.fabrics.GetFabricSuppliersUseCase
import pl.sofantastica.domain.usecase.fabrics.GetFabricsBySupplierUseCase
import pl.sofantastica.domain.usecase.fabrics.LoadFabricsUseCase
import javax.inject.Inject

@HiltViewModel
class FabricSelectorViewModel @Inject constructor(
    private val getSuppliers: GetFabricSuppliersUseCase,
    private val loadFabrics: LoadFabricsUseCase,
    private val getFabricsBySupplier: GetFabricsBySupplierUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var fabricSuppliers by mutableStateOf<List<String>>(emptyList())
    var fabricsBySuppliers by mutableStateOf<List<List<FabricDto>>>(emptyList())

    init {
        viewModelScope.launch {
            try {
                loadFabrics()
                fabricSuppliers = getSuppliers()
                fabricsBySuppliers = fabricSuppliers.map{ supplier ->
                    getFabricsBySupplier(supplier)
                }
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }
}
