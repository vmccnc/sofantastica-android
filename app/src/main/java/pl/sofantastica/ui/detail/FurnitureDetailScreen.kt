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

@Composable
fun FurnitureDetailRoute(
    id: Int,
    viewModel: FurnitureDetailViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    androidx.compose.runtime.LaunchedEffect(id) { viewModel.load(id) }
    when (val state = viewModel.uiState) {
        pl.sofantastica.ui.common.UiState.Loading -> Text("Loading...")
        is pl.sofantastica.ui.common.UiState.Error -> Text("Error: ${state.throwable.message}")
        is pl.sofantastica.ui.common.UiState.Success -> FurnitureDetailScreen(state.data)
    }
}
