package pl.sofantastica.ui.loading

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
import pl.sofantastica.domain.exceptions.ResIdException
import pl.sofantastica.domain.usecase.loading.LoadDataUseCase
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loadData: LoadDataUseCase
) : ViewModel() {
    var isDataLoaded by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                loadData()
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            } finally {
                isDataLoaded = true
            }
        }
    }
}