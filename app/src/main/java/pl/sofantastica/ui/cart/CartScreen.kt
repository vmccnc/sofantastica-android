package pl.sofantastica.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.sofantastica.R
import pl.sofantastica.data.model.CartItemDto

@Composable
fun CartRoute(userId: String, goToCatalog: () -> Unit, viewModel: CartViewModel = hiltViewModel()) {
    LaunchedEffect(userId) { viewModel.load(userId) }
    CartScreen(viewModel.items, { id -> viewModel.removeItem(id, userId) }, goToCatalog)
}

@Composable
fun CartScreen(items: List<CartItemDto>,
               onDelete: (Int) -> Unit,
               goToCatalog: () -> Unit
) {

    if (items.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(CartViewModel.EMPTY_DATA, textAlign = TextAlign.Center)
            TextButton(onClick = goToCatalog) {
                Text(stringResource(R.string.go_to_catalog), textAlign = TextAlign.Center)
            }
        }
        return
    }

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
