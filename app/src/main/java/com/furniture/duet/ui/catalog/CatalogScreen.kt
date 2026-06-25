package com.furniture.duet.ui.catalog

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderDefaults.colors
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.PagingData
import com.furniture.duet.R
import com.furniture.duet.data.model.SortOption
import com.furniture.duet.data.model.furniture.FurnitureCatalogModel
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.main.LoadingUI
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.theme.ActiveSliderColor
import com.furniture.duet.ui.theme.EnabledBtnColor
import com.furniture.duet.ui.theme.FurnitureCardBackgroundColor
import com.furniture.duet.ui.theme.InactiveSliderColor
import com.furniture.duet.ui.theme.SearchBarBackgroundColor
import com.furniture.duet.ui.theme.SelectedPageColor

@Composable
fun CatalogRoute(
    onItemClick: (Int) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    when (val state = viewModel.uiState) {
        is UiState.Loading -> {
            LoadingUI()
        }
        is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
        is UiState.Success ->
            CatalogScreen(onItemClick)
    }
}

@Composable
fun CatalogScreen(
    onItemClick: (Int) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val size_75 = dimensionResource(R.dimen.size_75)
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val margin_12 = dimensionResource(R.dimen.margin_12)
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_20 = dimensionResource(R.dimen.margin_20)

    if (viewModel.isSortDialogOpened) {
        ChooseSortDialog(viewModel.selectedSort, viewModel::onSetSortOption)
    }

    if (viewModel.isChoosePriceRangeDialogOpened) {
        ChoosePriceRangeDialog(
            viewModel.currentMinPrice,
            viewModel.currentMaxPrice,
            viewModel.minPrice,
            viewModel.maxPrice,
            viewModel::onSetPriceRange
        )
    }
    var columnsCount = LocalConfiguration.current.screenWidthDp / 170
    if (columnsCount > 3) columnsCount = 3

    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsCount),
        modifier = Modifier.padding(margin_16),
        verticalArrangement = Arrangement.spacedBy(margin_16),
        horizontalArrangement = Arrangement.spacedBy(margin_16)
    ) {
        val data = (viewModel.uiState as UiState.Success).data
        item(span = { GridItemSpan(columnsCount) }) {
            Column {
                Text(text = stringResource(R.string.catalog_label),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = viewModel.searchQuery,
                    onValueChange = viewModel::onSetSearchQuery,
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.i_search),
                            contentDescription = null
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = {
                        Text(text = stringResource(R.string.search_placeholder),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SearchBarBackgroundColor,
                        unfocusedContainerColor = SearchBarBackgroundColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            viewModel.onSearch()
                        }
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = margin_10)
                        .clip(shape = RoundedCornerShape(margin_20))
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = margin_10),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(margin_16, alignment = Alignment.CenterHorizontally)
                ) {
                    items(viewModel.categories) { item ->
                        val backgroundColor =
                            if (item.id == viewModel.selectedCategory) SelectedPageColor
                            else Color.White
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = item.title,
                                modifier = Modifier
                                    .border(2.dp, backgroundColor, CircleShape)
                                    .background(Color.White, CircleShape)
                                    .size(size_75)
                                    .padding(margin_12)
                                    .clickable { viewModel.selectCategory(item.id) }
                            )
                            Text(
                                item.title,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.openSortDialog() }
                    ) {
                        Text(
                            modifier = Modifier.padding(end = margin_10),
                            style = MaterialTheme.typography.titleSmall,
                            text = stringResource(R.string.sort_label)
                        )
                        Text(
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Black,
                            text = stringResource(viewModel.selectedSort.textId)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.openPriceRangeDialog() }
                    ) {
                        Text(
                            modifier = Modifier.padding(end = margin_10),
                            style = MaterialTheme.typography.titleSmall,
                            text = stringResource(R.string.price_range_label)
                        )
                        Text(
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Black,
                            text = stringResource(R.string.price_range)
                                .format(viewModel.currentMinPrice.toInt(), viewModel.currentMaxPrice.toInt())
                        )
                    }
                }
                if (data.list.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(margin_16).fillMaxSize(),
                        text = stringResource(R.string.empty_catalog),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        items(data.list) { item ->
            FurnitureCard(item, onItemClick, viewModel::onToggleFavorite)
        }
        item(span = { GridItemSpan(columnsCount) }) {
            if (data.isLast || data.list.isEmpty()) return@item
            TextButton(
                onClick = viewModel::loadNextPage,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = EnabledBtnColor
                )
            ) {
                Text(
                    text = stringResource(R.string.load_more),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun FurnitureCard(
    item: FurnitureCatalogModel,
    onItemClick: (Int) -> Unit,
    onSetFavorite: (FurnitureCatalogModel) -> Unit,
) {
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val margin_20 = dimensionResource(R.dimen.margin_20)

    ConstraintLayout(
        modifier = Modifier
            .background(FurnitureCardBackgroundColor, RoundedCornerShape(margin_20))
            .clickable { onItemClick(item.id) }
    ) {
        val (image, favBtn, nameTxt, priceTxt) = createRefs()

        AsyncImage(
            modifier = Modifier
                .height(dimensionResource(R.dimen.size_170))
                .clip(RoundedCornerShape(topStart = margin_20, topEnd = margin_20))
                .constrainAs(image) { top.linkTo(parent.top) },
            model = item.imageUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.no_image)
        )

        //var isFavorite by remember { mutableStateOf(item.isFavorite) }

        val favoriteIcon =
            if(item.isFavorite) painterResource(R.drawable.i_favorite)
            else painterResource(R.drawable.i_favorite_border)

        Icon(
            painter = favoriteIcon,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White)
                .clickable {
                    onSetFavorite(item)
                }
                .constrainAs(favBtn) {
                    top.linkTo(parent.top, margin = margin_10)
                    end.linkTo(parent.end, margin = margin_10)
                }
        )
        Text(
            text = item.name,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(top = margin_10, start = margin_10, end = margin_10)
                .constrainAs(nameTxt) {
                    top.linkTo(image.bottom)
                    start.linkTo(parent.start)
                },
            color = Color.White,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = stringResource(R.string.furniture_total_price).format(item.basePrice),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(start = margin_10, bottom = margin_10)
                .constrainAs(priceTxt) {
                    top.linkTo(nameTxt.bottom)
                    start.linkTo(parent.start)
                },
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePriceRangeDialog(
    currentMinPrice: Float,
    currentMaxPrice: Float,
    minPrice: Float,
    maxPrice: Float,
    onSetPriceRange: (Float, Float) -> Unit,
) {
    val size_5 = dimensionResource(R.dimen.margin_5)
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    var sliderPosition by remember { mutableStateOf(currentMinPrice..currentMaxPrice) }

    val startInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val endInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val sliderColors = colors(
        thumbColor = ActiveSliderColor,
        activeTrackColor = ActiveSliderColor,
        activeTickColor = ActiveSliderColor,
        inactiveTrackColor = InactiveSliderColor,
        inactiveTickColor = InactiveSliderColor
    )
    Dialog(
        onDismissRequest = {onSetPriceRange(currentMinPrice, currentMaxPrice)}
    ) {
        Column(Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(margin_20))
            .padding(margin_10)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = margin_10),
                Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.filter_price)
                        .format(sliderPosition.start.toInt()),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    color = EnabledBtnColor
                )
                Text(
                    text = stringResource(R.string.filter_price)
                        .format(sliderPosition.endInclusive.toInt()),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    color = EnabledBtnColor
                )
            }
            RangeSlider(
                value = sliderPosition,
                onValueChange = { range -> sliderPosition = range },
                valueRange = minPrice..maxPrice,
                onValueChangeFinished = {},
                colors = sliderColors,
                startThumb = {
                    PriceRangeThumb(
                        //currentPrice = sliderPosition.start,
                        interactionSource = startInteractionSource
                    )
                },
                endThumb = {
                    PriceRangeThumb(
                        //currentPrice = sliderPosition.endInclusive,
                        interactionSource = endInteractionSource
                    )
                },
                track = { sliderState ->
                    SliderDefaults.Track(
                        rangeSliderState = sliderState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(size_5),
                        thumbTrackGapSize = 0.dp,
                        trackInsideCornerSize = 0.dp,
                        colors = sliderColors
                    )
                }
            )

            TextButton(
                onClick = { onSetPriceRange(sliderPosition.start, sliderPosition.endInclusive) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnabledBtnColor,
                    contentColor = Color.White
                )
            ) { Text(stringResource(R.string.set_price_range)) }
        }
    }
}

@Composable
fun PriceRangeThumb(
    interactionSource: MutableInteractionSource
) {
    val interactions = remember { mutableStateListOf<Interaction>() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> interactions.add(interaction)
                is PressInteraction.Release -> interactions.remove(interaction.press)
                is PressInteraction.Cancel -> interactions.remove(interaction.press)
                is DragInteraction.Start -> interactions.add(interaction)
                is DragInteraction.Stop -> interactions.remove(interaction.start)
                is DragInteraction.Cancel -> interactions.remove(interaction.start)
            }
        }
    }
    Spacer(
        Modifier
            .size(30.dp)
            .hoverable(interactionSource = interactionSource)
            .background(ActiveSliderColor, CircleShape)
    )
}

@Composable
fun ChooseSortDialog(
    selectedSort: SortOption,
    onSet: (SortOption) -> Unit
) {
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    Dialog(onDismissRequest = {onSet(selectedSort)}) {
        Column(Modifier
            .background(Color.White, RoundedCornerShape(margin_20))
            .padding(margin_10)
        ) {
            SortOption.entries.forEach { sortOption ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (sortOption == selectedSort),
                            onClick = { onSet(sortOption) },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (sortOption == selectedSort),
                        onClick = { onSet(sortOption) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = EnabledBtnColor,
                            unselectedColor = EnabledBtnColor
                        )
                    )
                    Text(
                        text = stringResource(sortOption.textId),
                        style = MaterialTheme.typography.labelSmall,
                        color = EnabledBtnColor,
                        modifier = Modifier.padding(end = margin_10)
                    )
                }
            }
        }
    }
}