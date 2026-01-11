package pl.sofantastica.ui.fabricselector

import android.app.Dialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import pl.sofantastica.R
import pl.sofantastica.data.model.FabricDto
import pl.sofantastica.ui.ErrorUI
import pl.sofantastica.ui.LoadingUI
import pl.sofantastica.ui.common.UiState
import pl.sofantastica.ui.detail.FurnitureDetailScreen
import pl.sofantastica.ui.fabrics.FabricsViewModel

@Composable
fun FabricSelectorScreen(
    closeDialog: () -> Unit,
    chooseFabric: (FabricDto) -> Unit,
    viewModel: FabricSelectorViewModel = hiltViewModel()
) {

    val pagerState = rememberPagerState(pageCount = { viewModel.fabricsBySuppliers.size })
    var tabState by remember { mutableStateOf(0) }

    if (viewModel.fabricsBySuppliers.isNotEmpty()) {
        Dialog(onDismissRequest = closeDialog) {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                TabRow(
                    selectedTabIndex = 0,
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        )
                    }
                ) {
                    viewModel.fabricSuppliers.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                pagerState.requestScrollToPage(index)
                                tabState = index
                            },
                            text = { Text(text = title) },
                        )
                    }
                }

                HorizontalPager(state = pagerState) { page ->
                    LazyColumn {
                        items(viewModel.fabricsBySuppliers[page]) { fabric ->
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { chooseFabric(fabric) }) {
                                AsyncImage(
                                    model = fabric.fabricUrl,
                                    contentDescription = fabric.name,
                                    error = painterResource(R.drawable.no_image),
                                    modifier = Modifier.padding(5.dp)
                                )
                                Text(fabric.name, modifier = Modifier.padding(5.dp))
                                Text(stringResource(R.string.fabric_price).format(fabric.price))
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}
