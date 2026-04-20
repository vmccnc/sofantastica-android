package com.furniture.duet.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.furniture.duet.R
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.main.LoadingUI
import com.furniture.duet.ui.catalog.FurnitureCard
import com.furniture.duet.ui.common.UiState

@Composable
fun FavoritesRoute(onItemClick: (Int) -> Unit,
                   goToCatalog: () -> Unit,
                   viewModel: FavoritesViewModel = hiltViewModel()
) {
    when (val state = viewModel.uiState) {
        is UiState.Loading -> {
            LoadingUI()
            viewModel.load()
        }
        is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
        is UiState.Success ->
            FavoritesScreen(
                furniture = state.data,
                onItemClick = onItemClick,
                onToggleFavorite = viewModel::toggleFavorite,
                goToCatalog = goToCatalog
            )
    }
}

@Composable
fun FavoritesScreen(
    furniture: List<FurnitureCatalogModel>,
    onItemClick: (Int) -> Unit,
    onToggleFavorite: (FurnitureCatalogModel) -> Unit,
    goToCatalog: () -> Unit
) {
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_5 = dimensionResource(R.dimen.margin_5)
    if (furniture.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(stringResource(R.string.no_favorites), textAlign = TextAlign.Center)
            TextButton(onClick = goToCatalog) {
                Text(stringResource(R.string.go_to_catalog), textAlign = TextAlign.Center)
            }
        }
        return
    }
    var columnsCount = LocalConfiguration.current.screenWidthDp / 170
    if (columnsCount > 3) columnsCount = 3

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize().padding(margin_16),
        columns = GridCells.Fixed(columnsCount),
        verticalArrangement = Arrangement.spacedBy(margin_16),
        horizontalArrangement = Arrangement.spacedBy(margin_16)
    ) {
        item(span = { GridItemSpan(columnsCount) }) {
            Text(text = stringResource(R.string.favorites_label),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = margin_5)
            )
        }
        items(furniture) { item ->
            FurnitureCard(item, onItemClick, { onToggleFavorite(item) })
        }
    }
}