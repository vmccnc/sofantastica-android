package com.furniture.duet.ui.fabricselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.furniture.duet.R
import com.furniture.duet.data.model.fabric.FabricDto
import com.furniture.duet.data.model.fabric.FabricSelectorModel
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.main.LoadingUI
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.theme.EnabledBtnColor
import com.furniture.duet.ui.theme.FabricPrimaryColor
import com.furniture.duet.ui.theme.FabricSecondaryColor
import com.furniture.duet.ui.theme.LightBackground
import com.furniture.duet.ui.theme.SelectedSupplierColor

@Composable
fun FabricSelectorRoute(
    closeDialog: () -> Unit,
    chooseFabric: (FabricDto?) -> Unit,
    viewModel: FabricSelectorViewModel = hiltViewModel()
) {
    Dialog(
        onDismissRequest = closeDialog
    ) {
        when (val state = viewModel.uiState) {
            is UiState.Loading -> LoadingUI()
            is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
            is UiState.Success -> FabricSelectorScreen(
                chooseFabric,
                viewModel::selectFabric,
                closeDialog,
                state.data,
                viewModel.selectedFabric
            )
        }
    }
}

@Composable
fun FabricSelectorScreen(
    chooseFabric: (FabricDto?) -> Unit,
    selectFabric: (FabricDto) -> Unit,
    closeDialog: () -> Unit,
    data: FabricSelectorModel,
    selectedFabric: FabricDto?
) {
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    val margin_60 = dimensionResource(R.dimen.margin_60)
    val roundedShape = RoundedCornerShape(margin_60)

    val pagerState = rememberPagerState(pageCount = { data.fabricsBySuppliers.size })
    var tabState by remember { mutableStateOf(0) }

    if (data.fabricsBySuppliers.isNotEmpty()) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(margin_20))
                .padding(margin_16)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    stringResource(R.string.select_fabric),
                    color = FabricSecondaryColor,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(margin_10)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.i_close),
                    contentDescription = null,
                    modifier = Modifier.clickable { closeDialog() }
                )
            }

            TabRow(
                selectedTabIndex = 0,
                indicator = {},
                divider = {},
                containerColor = Color.Transparent
            ) {
                data.suppliers.forEachIndexed { index, title ->
                    var tabModifier: Modifier = Modifier.clip(roundedShape)
                    var textColor: Color = FabricPrimaryColor
                    if (pagerState.currentPage == index) {
                        textColor = Color.White
                        tabModifier = Modifier
                            .background(SelectedSupplierColor, shape = roundedShape)
                    }
                    Tab(
                        selected = pagerState.currentPage == index,
                        modifier = tabModifier,
                        onClick = {
                            pagerState.requestScrollToPage(index)
                            tabState = index
                        },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.displayMedium,
                                color = textColor
                            )
                        }
                    )
                }
            }

            HorizontalPager(state = pagerState) { page ->
                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    items(data.fabricsBySuppliers[page]) { fabric ->
                        FabricItem(fabric, selectFabric, fabric == selectedFabric)
                    }
                    item {
                        TextButton(
                            onClick = { chooseFabric(selectedFabric) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EnabledBtnColor,
                                contentColor = Color.White
                            ),
                            shape = roundedShape
                        ) {
                            Text(
                                text = stringResource(R.string.confirm_the_selection),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FabricItem(item: FabricDto,
               selectFabric: (FabricDto) -> Unit,
               isSelected: Boolean
) {
    val margin_5 = dimensionResource(R.dimen.margin_5)
    val margin_60 = dimensionResource(R.dimen.margin_60)
    val roundedShape = RoundedCornerShape(margin_60)
    val backgroundColor =
        if (isSelected) LightBackground
        else Color.Transparent
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = margin_5)
            .clip(roundedShape)
            .clickable { selectFabric(item) }
            .background(backgroundColor, roundedShape)
    ) {
        val (image, nameTxt, priceTxt) = createRefs()
        AsyncImage(
            model = item.fabricUrl,
            contentDescription = item.name,
            error = painterResource(R.drawable.no_image),
            modifier = Modifier
                .clip(CircleShape)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        Text(item.name,
            style = MaterialTheme.typography.titleSmall,
            color = FabricPrimaryColor,
            modifier = Modifier.constrainAs(nameTxt) {
                start.linkTo(image.end, margin = 10.dp)
                top.linkTo(parent.top, margin = 5.dp)
                bottom.linkTo(priceTxt.top)
            },
        )
        Text(stringResource(R.string.selected_fabric_price).format(item.price),
            style = MaterialTheme.typography.titleSmall,
            color = FabricSecondaryColor,
            modifier = Modifier.constrainAs(priceTxt) {
                start.linkTo(image.end, margin = 10.dp)
                bottom.linkTo(parent.bottom, margin = 5.dp)
                top.linkTo(nameTxt.bottom)
            }
        )
    }
}