package com.furniture.duet.ui.orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCbrt
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.furniture.duet.R
import com.furniture.duet.data.model.order.OrderDto
import com.furniture.duet.data.model.order.OrderHistoryModel
import com.furniture.duet.data.model.order.OrderItemDto
import com.furniture.duet.data.model.order.OrderModel
import com.furniture.duet.ui.cart.CartCounter
import com.furniture.duet.ui.cart.CartScreen
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.main.LoadingUI
import com.furniture.duet.ui.theme.EnabledBtnColor
import com.furniture.duet.ui.theme.FabricSecondaryColor
import com.furniture.duet.ui.theme.FurnitureDetailTextColor
import com.furniture.duet.ui.theme.InactiveSliderColor
import com.furniture.duet.ui.theme.LightBackground

@Composable
fun OrderHistoryScreen(
    viewModel: OrderHistoryViewModel = hiltViewModel()
) {
    when (val state = viewModel.uiState) {
        is UiState.Loading -> LoadingUI()
        is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
        is UiState.Success<OrderHistoryModel> -> OrderHistoryContent(state.data, viewModel::loadNextPage)
    }
}

@Composable
fun OrderHistoryContent(
   data: OrderHistoryModel,
   loadNextPage: () -> Unit
) {
    val margin_16 = dimensionResource(R.dimen.margin_16)
    if (data.orders.isEmpty()) {
        Text(
            modifier = Modifier.padding(margin_16).fillMaxSize(),
            text = stringResource(R.string.empty_orders),
            textAlign = TextAlign.Center
        )
        return
    }
    Text(
        text = stringResource(R.string.my_orders),
        modifier = Modifier.padding(top = margin_16, start = margin_16)
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = margin_16)) {
        for (order in data.orders) {
            OrderContent(order)
        }
        if (data.isLast) return
        TextButton(
            onClick = loadNextPage,
            modifier = Modifier.fillMaxWidth(),
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

@Composable
fun OrderContent(order: OrderModel) {
    val size_5 = dimensionResource(R.dimen.margin_5)
    val size_8 = dimensionResource(R.dimen.margin_8)
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val radius_64 = dimensionResource(R.dimen.size_64)
    var showDetails by remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(bottom = margin_16)) {
        order.items.forEach { item ->
            OrderItemContent(item)
        }
        Row(Modifier
            .fillMaxWidth()
            .padding(vertical = margin_16), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(
                text = stringResource(order.status.textId),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .background(InactiveSliderColor, RoundedCornerShape(radius_64))
                    .padding(vertical = size_8, horizontal = margin_16)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showDetails = !showDetails }
            ) {
                Text(
                    text = stringResource(R.string.more),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = size_5)
                )
                val arrowPainter =
                    if (showDetails) painterResource(R.drawable.i_arrow_up)
                    else painterResource(R.drawable.i_arrow_down)
                Image(
                    painter = arrowPainter,
                    modifier = Modifier
                        .width(size_8)
                        .height(size_5),
                    colorFilter = ColorFilter.tint(Color.Black),
                    contentDescription = null
                )
            }
        }
        if (showDetails) {
            val detailColor = Color.Black.copy(.65f)
            Row(Modifier
                .fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(R.string.payment_method),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(order.typeOfPayment.textId),
                    color = detailColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(Modifier
                .fillMaxWidth()
                .padding(top = margin_16), Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(R.string.delivery_method),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End
                )
                Text(
                    text = stringResource(order.typeOfDelivery.textId),
                    color = detailColor,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End
                )
            }
            Row(Modifier
                .fillMaxWidth()
                .padding(top = margin_16), Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(R.string.delivery_address),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${order.address}; ${order.city}; ${order.country}; ${order.postCode}",
                    color = detailColor,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun OrderItemContent(item: OrderItemDto) {
    val margin_5 = dimensionResource(R.dimen.margin_5)
    val margin_8 = dimensionResource(R.dimen.margin_8)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    val margin_32 = dimensionResource(R.dimen.margin_32)
    val size_24 = dimensionResource(R.dimen.size_24)
    val size_130 = dimensionResource(R.dimen.size_130)
    val titleSize = LocalConfiguration.current.screenWidthDp - 200
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = margin_32)
    ) {
        val (furnitureImage, furnitureName, fabricName, fabricImage, price, amount) = createRefs()

        AsyncImage(
            modifier = Modifier
                .size(size_130)
                .padding(end = margin_5)
                .clip(RoundedCornerShape(margin_20))
                .constrainAs(furnitureImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            model = item.furnitureUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.no_image)
        )

        Text(
            modifier = Modifier
                .width(titleSize.dp)
                .constrainAs(furnitureName) {
                    top.linkTo(parent.top)
                    start.linkTo(furnitureImage.end, size_24)
                },
            text = item.furnitureName,
            color = FurnitureDetailTextColor,
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            modifier = Modifier
                .constrainAs(price) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
            text = stringResource(R.string.furniture_total_price).format(item.finalPrice),
            color = FurnitureDetailTextColor,
            style = MaterialTheme.typography.titleSmall
        )

        AsyncImage(
            modifier = Modifier
                .size(size_24)
                .clip(CircleShape)
                .constrainAs(fabricImage) {
                    top.linkTo(fabricName.top)
                    bottom.linkTo(fabricName.bottom)
                    start.linkTo(furnitureImage.end, size_24)
                },
            model = item.fabricUrl,
            contentDescription = null,
            error = painterResource(R.drawable.no_image)
        )
        Text(
            modifier = Modifier
                .padding(margin_8)
                .constrainAs(fabricName) {
                    top.linkTo(furnitureName.bottom)
                    start.linkTo(fabricImage.end)
                },
            text = item.fabricName,
            color = FabricSecondaryColor,
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            modifier = Modifier
                .padding(margin_8)
                .constrainAs(amount) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(furnitureImage.end, size_24)
                },
            text = stringResource(R.string.order_amount).format(item.amountOfItems),
            color = FabricSecondaryColor,
            style = MaterialTheme.typography.titleSmall
        )
    }
}