package com.furniture.duet.ui.fabricselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.furniture.duet.R
import com.furniture.duet.data.model.fabric.FabricDto
import com.furniture.duet.data.model.fabric.FabricSelectorModel
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.main.LoadingUI
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.theme.FabricPrimaryColor
import com.furniture.duet.ui.theme.FabricSecondaryColor
import com.furniture.duet.ui.theme.SelectedSupplierColor

@Composable
fun FabricSelectorRoute(
    closeDialog: () -> Unit,
    chooseFabric: (FabricDto) -> Unit,
    viewModel: FabricSelectorViewModel = hiltViewModel()
) {
    Dialog(onDismissRequest = closeDialog) {
        when (val state = viewModel.uiState) {
            is UiState.Loading -> LoadingUI()
            is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
            is UiState.Success -> FabricSelectorScreen(
                chooseFabric,
                closeDialog,
                state.data
            )
        }
    }
}

@Composable
fun FabricSelectorScreen(
    chooseFabric: (FabricDto) -> Unit,
    closeDialog: () -> Unit,
    data: FabricSelectorModel
) {

    val pagerState = rememberPagerState(pageCount = { data.fabricsBySuppliers.size })
    var tabState by remember { mutableStateOf(0) }

    if (data.fabricsBySuppliers.isNotEmpty()) {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    stringResource(R.string.select_fabric),
                    color = FabricSecondaryColor,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(10.dp)
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
                containerColor = MaterialTheme.colorScheme.background
            ) {
                data.fabricSuppliers.forEachIndexed { index, title ->
                    var tabModifier: Modifier = Modifier
                    var textColor: Color = FabricPrimaryColor
                    if (pagerState.currentPage == index) {
                        textColor = Color.White
                        tabModifier = Modifier
                            .background(SelectedSupplierColor, shape = RoundedCornerShape(50.dp))
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
                                style = MaterialTheme.typography.labelSmall,
                                color = textColor
                            )
                        },
                    )
                }
            }

            HorizontalPager(state = pagerState) { page ->
                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    items(data.fabricsBySuppliers[page]) { fabric ->
                        FabricItem(fabric, chooseFabric)
                    }
                }
            }
        }
    }
}

@Composable
fun FabricItem(item: FabricDto, chooseFabric: (FabricDto) -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth().clickable { chooseFabric(item) }) {
        val (image, nameTxt, priceTxt) = createRefs()
        AsyncImage(
            model = item.fabricUrl,
            contentDescription = item.name,
            error = painterResource(R.drawable.no_image),
            modifier = Modifier
                .clip(CircleShape)
                .constrainAs(image) {
                    start.linkTo(parent.start, margin = 5.dp)
                    top.linkTo(parent.top, margin = 5.dp)
                    bottom.linkTo(parent.bottom, margin = 5.dp)
                }
        )
        Text(item.name,
            style = MaterialTheme.typography.titleSmall,
            color = FabricPrimaryColor,
            modifier = Modifier.constrainAs(nameTxt) {
                start.linkTo(image.end, margin = 10.dp)
                top.linkTo(parent.top, margin = 5.dp)
                bottom.linkTo(priceTxt.top)
            }
        )
        Text(stringResource(R.string.fabric_price).format(item.price),
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