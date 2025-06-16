package pl.sofantastica.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.navigation.NavHostController
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.ui.ErrorUI
import pl.sofantastica.ui.LoadingUI
import pl.sofantastica.ui.common.UiState


@Composable
fun FurnitureDetailRoute(
    navController: NavHostController,
    viewModel: FurnitureDetailViewModel = hiltViewModel()
) {
    when (val state = viewModel.uiState) {
        is UiState.Loading -> LoadingUI() // Text("Loading...")
        is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
        is UiState.Success -> FurnitureDetailScreen(item = state.data, onBack = { navController.popBackStack() })
    }
}

@Composable
fun FurnitureDetailScreen(item: FurnitureDto, onBack: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = item.name, modifier = Modifier.padding(top = 8.dp))
        Text(text = item.description, modifier = Modifier.padding(top = 4.dp))
        Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
            Text("Back")
        }
    }
}

