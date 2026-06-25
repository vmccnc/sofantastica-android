package com.furniture.duet.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.furniture.duet.R
import com.furniture.duet.data.model.furniture.FurnitureFabricDto
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.main.LoadingUI
import com.furniture.duet.ui.cart.CartCounter
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.fabricselector.FabricSelectorRoute
import com.furniture.duet.ui.theme.DisabledBtnColor
import com.furniture.duet.ui.theme.EnabledBtnColor
import com.furniture.duet.ui.theme.FabricPrimaryColor
import com.furniture.duet.ui.theme.FabricSecondaryColor
import com.furniture.duet.ui.theme.FurnitureDetailTextColor
import com.furniture.duet.ui.theme.LightBackground
import com.furniture.duet.ui.theme.PriceBackgroundColor
import com.furniture.duet.ui.theme.SelectedPageColor
import com.furniture.duet.ui.theme.TitleColor
import com.furniture.duet.ui.theme.UnselectedPageColor


@Composable
fun FurnitureDetailRoute(
    id: Int,
    viewModel: FurnitureDetailViewModel = hiltViewModel()
) {
    if (viewModel.isDialogOpened) {
        FabricSelectorRoute(viewModel::closeDialog, viewModel::chooseFabric)
    } else {
        when (val state = viewModel.uiState) {
            is UiState.Loading -> {
                LoadingUI()
                viewModel.load(id)
            }
            is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
            is UiState.Success -> FurnitureDetailScreen(
                item = state.data
            )
        }
    }
}

@Composable
fun FurnitureDetailScreen(item: FurnitureFabricDto,
                          viewModel: FurnitureDetailViewModel = hiltViewModel()) {
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_5 = dimensionResource(R.dimen.margin_5)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val size_40 = dimensionResource(R.dimen.size_40)
    val size_24 = dimensionResource(R.dimen.size_24)
    val titleSize = LocalConfiguration.current.screenWidthDp - 200
    val imagesHeight = LocalConfiguration.current.screenHeightDp / 3

    ConstraintLayout(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = margin_16)
    ) {
        val (carouselLayout, dotIndicator,
            view3DBtn, nameTxt, priceTxt,
            totalPriceLabel, totalPrice,
            fabricLayout, favBtn,
            selectFabricBtn, addToCartBtn, descriptionLayout, dimensionsLayout) = createRefs()

        item.imageUrls.let { imageUrls ->
            val pagerState = rememberPagerState(pageCount = { imageUrls.size })
            HorizontalPager(
                modifier = Modifier
                    .heightIn(max = imagesHeight.dp)
                    .fillMaxWidth()
                    .constrainAs(carouselLayout) {
                        top.linkTo(parent.top)
                    },
                state = pagerState
            ) { page ->
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = imageUrls[page],
                    contentDescription = imageUrls[page],
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.no_image)
                )
            }
            Row(
                modifier = Modifier
                    .constrainAs(dotIndicator){
                        bottom.linkTo(carouselLayout.bottom, margin = margin_16)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) SelectedPageColor else UnselectedPageColor
                    Box(modifier = Modifier
                        .padding(horizontal = margin_16)
                        .clip(CircleShape)
                        .background(color)
                        .size(margin_10)
                    )
                }
            }
            val favBtnImage =
                if (viewModel.isFavorite()) painterResource(R.drawable.i_favorite)
                else painterResource(R.drawable.i_favorite_border)
            Image(
                modifier = Modifier
                    .padding(margin_10)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { viewModel.toggleFavorite() }
                    .constrainAs(favBtn) {
                        top.linkTo(carouselLayout.top)
                        end.linkTo(carouselLayout.end)
                    },
                painter = favBtnImage,
                contentDescription = null
            )

        }

        Button(
            onClick = {},
            enabled = false,
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = DisabledBtnColor,
                disabledContentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(view3DBtn) {
                    top.linkTo(carouselLayout.bottom, margin_5)
                }
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.i_cube),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.view_in_3d_btn_txt),
                style = MaterialTheme.typography.labelSmall
            )
        }

        Text(
            text = item.furnitureName,
            style = MaterialTheme.typography.titleMedium,
            color = FurnitureDetailTextColor,
            modifier = Modifier
                .padding(vertical = margin_20)
                .width(titleSize.dp)
                .constrainAs(nameTxt) {
                    top.linkTo(view3DBtn.bottom, margin_16)
                    start.linkTo(parent.start)
                    end.linkTo(priceTxt.start)
                }
        )

        Text(
            text = stringResource(R.string.furniture_total_price).format(item.basePrice),
            style = MaterialTheme.typography.titleMedium,
            color = FurnitureDetailTextColor,
            modifier = Modifier
                .background(PriceBackgroundColor, RoundedCornerShape(size_40))
                .padding(horizontal = margin_20, vertical = margin_20)
                .constrainAs(priceTxt) {
                    top.linkTo(view3DBtn.bottom, margin_16)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = stringResource(R.string.cart_total),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = FurnitureDetailTextColor,
            modifier = Modifier
                .padding(top = margin_16)
                .constrainAs(totalPriceLabel) {
                    top.linkTo(fabricLayout.bottom)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = stringResource(R.string.furniture_total_price).format(item.totalPrice),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = FurnitureDetailTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = margin_16)
                .constrainAs(totalPrice) {
                    top.linkTo(fabricLayout.bottom)
                    end.linkTo(parent.end)
                }
        )

        if (item.fabricId == 0) {
            InitialFabricSelectionText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = margin_5)
                    .constrainAs(fabricLayout) {
                        top.linkTo(nameTxt.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Button(
                onClick = viewModel::openDialog,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnabledBtnColor,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = margin_16)
                    .constrainAs(selectFabricBtn) {
                        top.linkTo(totalPriceLabel.bottom)
                    }
            ) {
                Text(
                    text = stringResource(R.string.select_fabric),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        } else {
            NotInitialFabricSelectionText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = margin_5)
                    .constrainAs(fabricLayout) {
                        top.linkTo(nameTxt.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                fabricName = item.fabricName,
                fabricUrl = item.fabricUrl,
                fabricPrice = item.fabricPrice
            )

            if(viewModel.count == 0) {
                NotAddedToCartButtons(
                    addToCart = viewModel::addToCart,
                    addToCartBtnModifier = Modifier
                        .fillMaxWidth()
                        .padding(top = margin_16)
                        .constrainAs(addToCartBtn) {
                            top.linkTo(totalPriceLabel.bottom)
                        },
                    openDialog = viewModel::openDialog,
                    openDialogBtnModifier = Modifier
                        .fillMaxWidth()
                        .padding(top = margin_16)
                        .border(1.dp, EnabledBtnColor, CircleShape)
                        .constrainAs(selectFabricBtn) {
                            top.linkTo(addToCartBtn.bottom)
                        }
                )
            } else {
                AddedToCartButtons(
                    layoutModifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(selectFabricBtn) {
                            top.linkTo(totalPriceLabel.bottom)
                        }
                        .padding(vertical = margin_16),
                    count = viewModel.count,
                    setCount = viewModel::setCountInCart
                )
            }
        }

        Description(
            layoutModifier = Modifier
                .fillMaxWidth()
                .padding(top = margin_16)
                .background(Color.White, RoundedCornerShape(size_24))
                .constrainAs(descriptionLayout) {
                    top.linkTo(selectFabricBtn.bottom)
                },
            isDescriptionOpened = viewModel.isDescriptionOpened,
            toggleDescription = viewModel::toggleDescription,
            descriptionText = item.description
        )

        Dimensions(
            layoutModifier = Modifier
                .fillMaxWidth()
                .padding(top = margin_16)
                .background(Color.White, RoundedCornerShape(size_24))
                .constrainAs(dimensionsLayout) {
                    top.linkTo(descriptionLayout.bottom)
                },
            isDimensionsOpened = viewModel.isDimensionsOpened,
            toggleDimensions = viewModel::toggleDimensions,
            width = 0,
            height = 0,
            depth = 0
        )

    }

}

@Composable
fun InitialFabricSelectionText(modifier: Modifier) {
    val margin_16 = dimensionResource(R.dimen.margin_16)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.i_question),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(start = margin_16),
            text = stringResource(R.string.please_choose_the_fabric),
            color = FurnitureDetailTextColor,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun NotInitialFabricSelectionText(
    modifier: Modifier,
    fabricName: String,
    fabricUrl: String,
    fabricPrice: Int
) {
    val margin_16 = dimensionResource(R.dimen.margin_16)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            model = fabricUrl,
            contentDescription = fabricUrl,
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.no_image)
        )
        Column(modifier = Modifier.padding(start = margin_16)) {
            Text(
                text = fabricName,
                color = FabricPrimaryColor,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = stringResource(R.string.selected_fabric_price).format(fabricPrice),
                color = FabricSecondaryColor,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun NotAddedToCartButtons(
    addToCart: () -> Unit,
    addToCartBtnModifier: Modifier,
    openDialog: () -> Unit,
    openDialogBtnModifier: Modifier
) {
    Button(
        onClick = addToCart,
        colors = ButtonDefaults.buttonColors(
            containerColor = EnabledBtnColor,
            contentColor = Color.White
        ),
        modifier = addToCartBtnModifier
    ) {
        Text(
            text = stringResource(R.string.add_to_cart),
            style = MaterialTheme.typography.labelSmall
        )
    }
    Button(
        onClick = openDialog,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = FabricPrimaryColor
        ),
        modifier = openDialogBtnModifier
    ) {
        Text(
            text = stringResource(R.string.change_fabric),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun AddedToCartButtons(
    layoutModifier: Modifier,
    count: Int,
    setCount: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = layoutModifier
    ) {
        Image(
            painter = painterResource(R.drawable.i_checked),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.product_added_to_cart),
            style = MaterialTheme.typography.bodyMedium,
            color = TitleColor,
            modifier = Modifier.fillMaxSize(.5f)
        )
        CartCounter(count, setCount)
    }
}

@Composable
fun Description(
    layoutModifier: Modifier,
    isDescriptionOpened: Boolean,
    toggleDescription: () -> Unit,
    descriptionText: String
) {
    val margin_16 = dimensionResource(R.dimen.margin_16)
    Column(
        modifier = layoutModifier
    ) {
        Row(
            modifier = Modifier
                .padding(margin_16)
                .clickable { toggleDescription() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.product_description),
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.weight(1f))
            val arrowPainter =
                if (isDescriptionOpened) painterResource(R.drawable.i_arrow_up)
                else painterResource(R.drawable.i_arrow_down)
            Image(
                painter = arrowPainter,
                contentDescription = null
            )
        }

        if (isDescriptionOpened) {
            Text(
                text = descriptionText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = margin_16, end = margin_16, bottom = margin_16)
            )
        }
    }
}

@Composable
fun Dimensions(
    layoutModifier: Modifier,
    isDimensionsOpened: Boolean,
    toggleDimensions: () -> Unit,
    width: Int,
    height: Int,
    depth: Int
) {
    val margin_16 = dimensionResource(R.dimen.margin_16)
    Column(
        modifier = layoutModifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(margin_16)
                .clickable { toggleDimensions() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.dimensions_label),
                style = MaterialTheme.typography.labelSmall
            )
            val arrowPainter =
                if (isDimensionsOpened) painterResource(R.drawable.i_arrow_up)
                else painterResource(R.drawable.i_arrow_down)
            Image(
                painter = arrowPainter,
                contentDescription = null
            )
        }

        if (isDimensionsOpened) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(margin_16),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.width_label),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    //text = width.toString(),
                    stringResource(R.string.undefined_width),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = margin_16),
                color = LightBackground
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(margin_16),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.height_label),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
//                    text = height.toString(),
                    text = stringResource(R.string.undefined_height),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = margin_16),
                color = LightBackground
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(margin_16),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.depth_label),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
//                    text = height.toString(),
                    text = stringResource(R.string.undefined_depth),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}