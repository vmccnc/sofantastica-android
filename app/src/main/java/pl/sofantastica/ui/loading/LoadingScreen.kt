package pl.sofantastica.ui.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoadingScreen(
    afterDataIsLoaded: () -> Unit,
    viewModel: LoadingViewModel = hiltViewModel()
) {
    if (viewModel.isDataLoaded) {
        afterDataIsLoaded()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}