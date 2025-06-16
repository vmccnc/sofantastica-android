package pl.sofantastica.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pl.sofantastica.data.model.FurnitureDto
import pl.sofantastica.ui.common.UiState


@Composable
fun FurnitureDetailRoute(
    id: Int,
    viewModel: FurnitureDetailViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    LaunchedEffect(id) {
        viewModel.load(id)
    }
    when (val state = viewModel.uiState) {
        is UiState.Loading -> Text("Loading...")
        is UiState.Error -> Text("Error: ${state.throwable.message}")
        is UiState.Success -> FurnitureDetailScreen(state.data)
    }
}

@Composable
fun FurnitureDetailScreen(item: FurnitureDto) {
    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = item.name, modifier = Modifier.padding(top = 8.dp))
        Text(text = item.description, modifier = Modifier.padding(top = 4.dp))
    }
}

