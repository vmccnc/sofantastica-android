package com.furniture.duet.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.furniture.duet.R
import com.furniture.duet.data.model.cart.CartItemModel
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.main.LoadingUI
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.main.MainViewModel
import com.furniture.duet.ui.orders.MakeOrderScreen
import com.furniture.duet.ui.theme.EnabledBtnColor
import com.furniture.duet.ui.theme.FabricSecondaryColor
import com.furniture.duet.ui.theme.FurnitureDetailTextColor
import com.furniture.duet.ui.theme.SearchBarBackgroundColor
import com.furniture.duet.ui.theme.TitleColor

@Composable
fun CartRoute(
    goToOrderHistory: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    when (val state = viewModel.uiState) {
        is UiState.Loading -> {
            LoadingUI()
            viewModel.load()
        }
        is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
        is UiState.Success ->
            CartScreen(goToOrderHistory)
    }
}

@Composable
fun CartScreen(
    goToOrderHistory: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val margin_16 = dimensionResource(R.dimen.margin_16)

    val data = (viewModel.uiState as UiState.Success).data

    if (data.items.isEmpty()) {
        ConstraintLayout(
            modifier = Modifier
                .padding(margin_16)
                .fillMaxSize()
        ) {
            val (cartLabel, emptyCartImage, emptyCartText) = createRefs()
            Text(text = stringResource(R.string.cart_label),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(cartLabel) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Image(
                modifier = Modifier
                    .constrainAs(emptyCartImage) {
                        top.linkTo(cartLabel.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                painter = painterResource(R.drawable.i_empty_cart),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .constrainAs(emptyCartText) {
                        top.linkTo(emptyCartImage.bottom, margin_16)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = stringResource(R.string.cart_is_empty),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn(modifier = Modifier.padding(margin_16)) {
            item {
                Column {
                    Text(text = stringResource(R.string.cart_label),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = stringResource(R.string.cart_products_count).format(data.items.count()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TitleColor,
                        modifier = Modifier.padding(vertical = margin_16)
                    )
                }
            }
            items(data.items) { item ->
                CartItem(
                    item,
                    viewModel::setCount,
                    viewModel::setFavorite,
                    viewModel::removeItem
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = margin_16),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.cart_total),
                        style = MaterialTheme.typography.bodyMedium,
                        color = FurnitureDetailTextColor
                    )
                    Text(
                        text = stringResource(R.string.furniture_total_price).format(data.total),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = FurnitureDetailTextColor
                    )
                }
                if (viewModel.makeOrder) {
                    MakeOrderScreen(data.total, goToOrderHistory)
                } else {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.makeOrder() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EnabledBtnColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.make_order),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun CartItem(
    item: CartItemModel,
    setCount: (Int, Int) -> Unit,
    setFavorite: (CartItemModel) -> Unit,
    remove: (Int) -> Unit
) {

    val margin_5 = dimensionResource(R.dimen.margin_5)
    val margin_8 = dimensionResource(R.dimen.margin_8)
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    val size_24 = dimensionResource(R.dimen.size_24)
    val size_40 = dimensionResource(R.dimen.size_40)
    val size_130 = dimensionResource(R.dimen.size_130)

    val width = LocalConfiguration.current.screenWidthDp.dp -
            margin_16*2 - margin_8*2 - size_130 - size_40*2

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = margin_8)
            .background(Color.White, RoundedCornerShape(margin_20))
            .padding(margin_8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(size_130)
                .padding(end = margin_5)
                .clip(RoundedCornerShape(margin_20)),
            model = item.furnitureUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.no_image)
        )
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    modifier = Modifier.width(width),
                    text = item.furnitureName,
                    color = FurnitureDetailTextColor,
                    style = MaterialTheme.typography.titleSmall
                )

                val favoriteIcon =
                    if(item.isFavorite) painterResource(R.drawable.i_favorite)
                    else painterResource(R.drawable.i_favorite_border)
                Image(
                    modifier = Modifier.clickable { setFavorite(item) },
                    painter = favoriteIcon,
                    contentDescription = null
                )

                Image(
                    modifier = Modifier.clickable { remove(item.id) },
                    painter = painterResource(R.drawable.i_trash),
                    contentDescription = null
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier.size(size_24).clip(CircleShape),
                    model = item.fabricUrl,
                    contentDescription = null,
                    error = painterResource(R.drawable.no_image)
                )
                Text(
                    modifier = Modifier.padding(margin_8),
                    text = item.fabricName,
                    color = FabricSecondaryColor,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CartCounter(
                    item.quantity,
                    { newCount -> setCount(item.id, newCount) }
                )
                Text(
                    modifier = Modifier.padding(start = margin_5),
                    text = stringResource(R.string.furniture_total_price).format(item.totalPrice),
                    color = FurnitureDetailTextColor,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.End
                )
            }

        }
    }
}

@Composable
fun CartCounter(
    count: Int,
    setCount: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val size_40 = dimensionResource(R.dimen.size_40)
    val roundedShape = RoundedCornerShape(size_40)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color.White, roundedShape)
            .border(1.dp, EnabledBtnColor, roundedShape)
    ) {
        Image(
            modifier = Modifier
                .padding(horizontal = margin_10)
                .clickable { setCount(count - 1) },
            painter = painterResource(R.drawable.i_minus),
            contentDescription = null
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = FurnitureDetailTextColor
        )
        Image(
            modifier = Modifier
                .padding(horizontal = margin_10)
                .clickable { setCount(count + 1) },
            painter = painterResource(R.drawable.i_add),
            contentDescription = null
        )
    }
}