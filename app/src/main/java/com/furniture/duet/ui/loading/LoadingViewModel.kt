package com.furniture.duet.ui.loading

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.furniture.duet.domain.usecase.loading.LoadDataUseCase
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.home.HomeViewModel
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val loadData: LoadDataUseCase
) : ViewModel() {
    var uiState by mutableStateOf<UiState<Unit>>(UiState.Loading)
        private set

    init {
        viewModelScope.launch {
            try {
                loadData()
                uiState = UiState.Success(Unit)
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }
}