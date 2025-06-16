package pl.sofantastica.ui.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.sofantastica.data.model.CartItemDto

@Composable
fun CartRoute(userId: String, viewModel: CartViewModel = hiltViewModel()) {
    LaunchedEffect(userId) { viewModel.load(userId) }
    CartScreen(viewModel.items) { id -> viewModel.removeItem(id, userId) }
}

@Composable
fun CartScreen(items: List<CartItemDto>, onDelete: (Int) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn {
            items(items) { item ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Text("Product ${'$'}{item.productId} qty ${'$'}{item.quantity}")
                    Button(onClick = { item.id?.let(onDelete) }) {
                        Text("Remove")
                    }
                }
            }
        }
    }
}
