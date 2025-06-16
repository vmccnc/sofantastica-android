package pl.sofantastica.ui.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.sofantastica.data.model.CategoryDto
import pl.sofantastica.data.model.FurnitureDto

@Composable
fun CatalogRoute(viewModel: CatalogViewModel = hiltViewModel()) {
    CatalogScreen(
        furniture = viewModel.furniture.collectAsState(initial = emptyList()).value,
        categories = viewModel.categories.collectAsState(initial = emptyList()).value,
        selected = viewModel.selectedCategory.collectAsState(initial = null).value,
        onSelectCategory = viewModel::selectCategory
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    furniture: List<FurnitureDto>,
    categories: List<CategoryDto>,
    selected: CategoryDto?,
    onSelectCategory: (CategoryDto?) -> Unit
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

        LazyColumn {
            items(furniture) { item ->
                Text(text = item.name, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp))
            }
        }
    }
}
