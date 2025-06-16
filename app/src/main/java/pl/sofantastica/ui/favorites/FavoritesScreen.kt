package pl.sofantastica.ui.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FavoritesRoute(userId: String, viewModel: FavoritesViewModel = hiltViewModel()) {
    LaunchedEffect(userId) {
        viewModel.load(userId)
    }
    FavoritesScreen(viewModel.favorites) { id ->
        viewModel.toggleFavorite(userId, id)
    }
}

@Composable
fun FavoritesScreen(favorites: List<pl.sofantastica.data.model.FurnitureDto>, onToggle: (Long) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn {
            items(favorites) { item ->
                Text(text = item.name, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp))
            }
        }
    }
}
