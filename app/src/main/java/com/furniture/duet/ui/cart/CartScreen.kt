package com.furniture.duet.ui.cart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.furniture.duet.R
import com.furniture.duet.data.model.cart.CartItemModel
import com.furniture.duet.ui.ErrorUI
import com.furniture.duet.ui.LoadingUI
import com.furniture.duet.ui.common.UiState

@Composable
fun CartRoute(goToCatalog: () -> Unit, viewModel: CartViewModel = hiltViewModel()) {
    viewModel.load()
    when (val state = viewModel.uiState) {
        is UiState.Loading -> {
            LoadingUI()
        }
        is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
        is UiState.Success ->
            CartScreen(state.data.items, state.data.total,
                viewModel::setCount,
                { id -> viewModel.removeItem(id) },
                goToCatalog)
    }
}

@Composable
fun CartScreen(items: List<CartItemModel>,
               total: Double,
               onSetCount: (Int, Int) -> Unit,
               onDelete: (Int) -> Unit,
               goToCatalog: () -> Unit
) {
    val margin_2 = dimensionResource(R.dimen.margin_2)
    val size_24 = dimensionResource(R.dimen.size_24)

    if (items.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.cart_is_empty), textAlign = TextAlign.Center)
            TextButton(onClick = goToCatalog) {
                Text(stringResource(R.string.go_to_catalog), textAlign = TextAlign.Center)
            }
        }
        return
    }

    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn(modifier = Modifier.fillMaxHeight(.9f)) {
            items(items) { item ->
                val scale = remember { Animatable(1f) }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .graphicsLayer {
                                scaleX = scale.value
                                scaleY = scale.value
                            }
                            .clickable {
                                scope.launch {
                                    scale.animateTo(0.95f, animationSpec = tween(100))
                                    scale.animateTo(1f, animationSpec = tween(100))
                                }
                            },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(8.dp)) {
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = null,
                                error = painterResource(R.drawable.no_image),
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(.9f)) {
                                Text(text = item.furnitureName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = stringResource(R.string.fabric).format(item.fabricName),
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2
                                )
                                Text(
                                    text = stringResource(R.string.furniture_price).format(item.price),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Icon(Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.clickable { onDelete(item.id) }
                            )
                        }
                        Row {
                            Icon(Icons.Default.KeyboardArrowLeft,
                                modifier = Modifier.size(size_24)
                                    .clickable { onSetCount(item.id, item.quantity - 1) },
                                contentDescription = null
                            )
                            Text(item.quantity.toString())
                            Icon(Icons.Default.KeyboardArrowRight,
                                modifier = Modifier.size(size_24)
                                    .clickable { onSetCount(item.id, item.quantity + 1) },
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.total).format(total),
                style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {  },
                shape = RectangleShape,
                modifier = Modifier.border(margin_2, MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = stringResource(R.string.make_order),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
