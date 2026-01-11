package pl.sofantastica.ui.fabrics

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sofantastica.domain.usecase.fabrics.GetFabricsUseCase
import javax.inject.Inject

@HiltViewModel
class FabricsViewModel @Inject constructor(
    private val getFabrics: GetFabricsUseCase
) : ViewModel() {

}
