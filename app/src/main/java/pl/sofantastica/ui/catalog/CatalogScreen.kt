package pl.sofantastica.ui.catalog

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
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureDto
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween

@Composable
fun CatalogRoute(
    onItemClick: (Int) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    CatalogScreen(
        furniture = viewModel.furniture,
        categories = viewModel.categories,
        selected = viewModel.selectedCategory,
        onSelectCategory = viewModel::selectCategory,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    furniture: List<FurnitureDto>,
    categories: List<CategoryDto>,
    selected: CategoryDto?,
    onSelectCategory: (CategoryDto?) -> Unit,
    onItemClick: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
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

        val scope = rememberCoroutineScope()
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
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}

