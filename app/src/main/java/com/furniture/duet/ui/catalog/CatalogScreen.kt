package com.furniture.duet.ui.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.furniture.duet.data.model.furniture.CategoryDto
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.furniture.duet.R
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.ui.ErrorUI
import com.furniture.duet.ui.LoadingUI
import com.furniture.duet.ui.common.UiState

@Composable
fun CatalogRoute(
    onItemClick: (Int) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    LaunchedEffect(viewModel.minPrice) {
        viewModel.loadFurniture()
    }
    LaunchedEffect(viewModel.maxPrice) {
        viewModel.loadFurniture()
    }
    when (val state = viewModel.uiState) {
        is UiState.Loading -> {
            LoadingUI()
        }
        is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
        is UiState.Success ->
            CatalogScreen(
                furniture = state.data,
                categories = viewModel.categories,
                selected = viewModel.selectedCategory,
                onSelectCategory = viewModel::selectCategory,
                isRefreshing = viewModel.isRefreshing,
                onRefresh = viewModel::refreshFurniture,
                onItemClick = onItemClick,
                onSetFavorite = viewModel::onSetFavorite,
                minPrice = viewModel.minPrice,
                onSetMinPrice = viewModel::onSetMinPrice,
                maxPrice = viewModel.maxPrice,
                onSetMaxPrice = viewModel::onSetMaxPrice
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    furniture: List<FurnitureCatalogModel>,
    categories: List<CategoryDto>,
    selected: CategoryDto?,
    onSelectCategory: (CategoryDto?) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (Int) -> Unit,
    onSetFavorite: (Int, Boolean) -> Unit,
    minPrice: String,
    onSetMinPrice: (String) -> Unit,
    maxPrice: String,
    onSetMaxPrice: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Row {
            val expanded = remember { mutableStateOf(false) }
            TextButton(onClick = { expanded.value = true }) {
                Text(text = selected?.name ?: "All")
            }
            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
                DropdownMenuItem(text = { Text("All") }, onClick = {
                    onSelectCategory(null)
                    expanded.value = false
                })
                categories.forEach { category ->
                    DropdownMenuItem(text = { Text(category.name) }, onClick = {
                        onSelectCategory(category)
                        expanded.value = false
                    })
                }
            }

            TextField(value = minPrice,
                onValueChange = onSetMinPrice,
                label = {Text("Min Price")},
                modifier = Modifier.padding(2.dp).weight(1f)
            )
            TextField(value = maxPrice,
                onValueChange = onSetMaxPrice,
                label = {Text("Max Price")},
                modifier = Modifier.padding(2.dp).weight(1f)
            )
        }

        val scope = rememberCoroutineScope()

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh
        ) {

            if (furniture.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.no_matching_data), textAlign = TextAlign.Center)
                }
                return@PullToRefreshBox
            }

            LazyColumn {
                items(furniture) { item ->
                    val scale = remember { Animatable(1f) }

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
                                    onItemClick(item.id)
                                }
                            },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(8.dp)) {
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = item.name,
                                error = painterResource(R.drawable.no_image),
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row {
                                    Text(text = item.name, modifier = Modifier.weight(.9f), style = MaterialTheme.typography.titleMedium)
                                    Icon(
                                        if(item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { onSetFavorite(item.id, !item.isFavorite) }
                                    )
                                }
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2
                                )
                                Text(
                                    text = stringResource(R.string.furniture_price).format(item.basePrice),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

