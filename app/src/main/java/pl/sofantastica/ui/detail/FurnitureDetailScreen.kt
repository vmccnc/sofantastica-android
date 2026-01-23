package pl.sofantastica.ui.detail

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import pl.sofantastica.R
import pl.sofantastica.data.model.FurnitureFabricDto
import pl.sofantastica.ui.ErrorUI
import pl.sofantastica.ui.LoadingUI
import pl.sofantastica.ui.common.UiState
import pl.sofantastica.ui.fabricselector.FabricSelectorScreen


@Composable
fun FurnitureDetailRoute(
    id: Int,
    viewModel: FurnitureDetailViewModel = hiltViewModel()
) {
    val isDialogOpened by viewModel.isDialogOpened.collectAsState()
    val count by viewModel.count.collectAsState()
    if (isDialogOpened) {
        FabricSelectorScreen(viewModel::closeDialog, viewModel::chooseFabric)
    } else {
        when (val state = viewModel.uiState) {
            is UiState.Loading -> {
                LoadingUI()
                viewModel.load(id)
            }
            is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
            is UiState.Success -> FurnitureDetailScreen(
                item = state.data,
                count = count,
                onSetCount = viewModel::setCount,
                onSelectFabric = viewModel::openDialog,
                onToggleFavorite = viewModel::toggleFavorite,
                addToCart = viewModel::addToCart
            )
        }
    }
}

@Composable
fun FurnitureDetailScreen(item: FurnitureFabricDto,
                          count: Int,
                          onSetCount: (Int) -> Unit,
                          onSelectFabric: () -> Unit,
                          onToggleFavorite: () -> Unit,
                          addToCart: () -> Unit) {
    val margin_16 = dimensionResource(R.dimen.margin_16)

    if (LocalConfiguration.current.orientation == ORIENTATION_PORTRAIT) {
        val carouselHeight = (LocalConfiguration.current.screenHeightDp / 4).dp
        Column(modifier = Modifier.padding(margin_16).verticalScroll(rememberScrollState())) {
            FurnitureDetailScreenContent(item, count, onSetCount, onSelectFabric, onToggleFavorite,
                addToCart, Modifier.fillMaxWidth().height(carouselHeight)
            )
        }
    } else {
        val carouselWidth = (LocalConfiguration.current.screenWidthDp / 2).dp
        Row(modifier = Modifier.padding(margin_16).verticalScroll(rememberScrollState())) {
            FurnitureDetailScreenContent(item, count, onSetCount, onSelectFabric, onToggleFavorite,
                addToCart, Modifier.fillMaxHeight().width(carouselWidth)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FurnitureDetailScreenContent(item: FurnitureFabricDto,
                                 count: Int,
                                 onSetCount: (Int) -> Unit,
                                 onSelectFabric: () -> Unit,
                                 onToggleFavorite: () -> Unit,
                                 addToCart: () -> Unit,
                                 carouselModifier: Modifier) {
    val margin_2 = dimensionResource(R.dimen.margin_2)
    val margin_5 = dimensionResource(R.dimen.margin_5)
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val size_46 = dimensionResource(R.dimen.size_46)
    val size_50 = dimensionResource(R.dimen.size_50)

    if (item.imageUrls.isNullOrEmpty()) {
        Image(painter = painterResource(R.drawable.no_image),
            contentDescription = null,
            modifier = carouselModifier
        )
    } else {
        HorizontalMultiBrowseCarousel(
            state = rememberCarouselState { item.imageUrls.count() },
            modifier = carouselModifier,
            preferredItemWidth = LocalConfiguration.current.screenWidthDp.dp
        ) { i ->
            val item = item.imageUrls[i]
            AsyncImage(
                modifier = Modifier.fillMaxSize().padding(margin_2),
                model = item.imageUrl,
                contentDescription = item.imageUrl,
                error = painterResource(R.drawable.no_image)
            )
        }
    }
    Column(modifier = Modifier.padding(margin_10)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = item.name, modifier = Modifier.weight(.9f), style = MaterialTheme.typography.titleLarge)

            Icon(if(item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(size_46).clickable { onToggleFavorite() }
            )
        }
        Row(modifier = Modifier.padding(top = margin_10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.category_label),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.width(margin_5))
            Text(text = item.category, style = MaterialTheme.typography.bodyMedium)
        }
        Row(modifier = Modifier.padding(top = margin_10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.base_price_label),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.width(margin_5))
            Text(text = stringResource(R.string.furniture_price).format(item.basePrice),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row(modifier = Modifier.padding(top = margin_10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.fabric_price_label),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.width(margin_5))
            Text(text = stringResource(R.string.furniture_price).format(item.fabricPrice),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row(modifier = Modifier.padding(top = margin_10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.total_price_label).format(item.totalPrice),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.width(margin_5))
            Text(text = stringResource(R.string.furniture_price).format(item.totalPrice),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(text = item.description, modifier = Modifier.padding(top = margin_10),
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.padding(top = margin_10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onSelectFabric,
                modifier = Modifier
                    .height(size_46)
                    .border(margin_2, MaterialTheme.colorScheme.secondary),
                shape = RectangleShape
            ) {
                Text(text = stringResource(R.string.select_fabric),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            AsyncImage(
                model = item.fabricUrl,
                contentDescription = stringResource(R.string.fabric_label),
                modifier = Modifier
                    .padding(start = margin_5)
                    .height(size_50)
                    .width(size_50)
            )
        }
        Row(modifier = Modifier.padding(top = margin_10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = addToCart,
                shape = RectangleShape,
                modifier = Modifier
                    .padding(vertical = margin_10)
                    .height(size_46)
                    .border(margin_2, MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = stringResource(R.string.add_to_cart),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Icon(Icons.Default.KeyboardArrowLeft,
                modifier = Modifier.size(size_46).clickable { onSetCount(count - 1) },
                contentDescription = null
            )
            Text(count.toString())
            Icon(Icons.Default.KeyboardArrowRight,
                modifier = Modifier.size(size_46).clickable { onSetCount(count + 1) },
                contentDescription = null
            )
        }
    }
}

