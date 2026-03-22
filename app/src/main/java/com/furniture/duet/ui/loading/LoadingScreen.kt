package com.furniture.duet.ui.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.common.UiState

@Composable
fun LoadingScreen(
    afterDataIsLoaded: () -> Unit,
    viewModel: LoadingViewModel = hiltViewModel()
) {
    when (val state = viewModel.uiState) {
        is UiState.Loading ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
        is UiState.Success -> afterDataIsLoaded()
    }
}