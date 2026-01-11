package pl.sofantastica.ui.loading

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
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
                isDataLoaded = loadData()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, LENGTH_LONG).show()
            }
        }
    }
}