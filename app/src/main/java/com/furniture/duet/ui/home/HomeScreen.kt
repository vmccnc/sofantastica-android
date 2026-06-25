package com.furniture.duet.ui.home

import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.FloatRange
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.LocalImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.furniture.duet.R
import com.furniture.duet.data.model.InfoModel
import com.furniture.duet.di.FirebaseModule
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.main.LoadingUI
import com.furniture.duet.ui.theme.AboutUsBackgroundColor
import com.furniture.duet.ui.theme.AboutUsTextColor
import com.furniture.duet.ui.theme.FurnitureDetailTextColor
import com.furniture.duet.ui.theme.MarcellusFontFamily
import com.furniture.duet.ui.theme.MondaFontFamily
import com.furniture.duet.ui.theme.SearchBarBackgroundColor
import com.furniture.duet.ui.theme.SelectedRouteBackgroundColor
import com.furniture.duet.ui.theme.TitleColor
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream
import kotlin.math.abs
import kotlin.math.sign

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val margin_5 = dimensionResource(R.dimen.margin_5)
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_20 = dimensionResource(R.dimen.margin_20)

    val  state = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(state), horizontalAlignment = Alignment.CenterHorizontally) {

        Column(modifier = Modifier.padding(margin_16), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.sofantastica_title),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(bottom = margin_20),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.comfort_style_for_your_home),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(top = margin_10),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.collection),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                softWrap = false
            )

            Row(
                modifier = Modifier.padding(bottom = margin_20),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier
                        .weight(.5f)
                        .padding(end = margin_5)
                        .clip(shape = RoundedCornerShape(margin_20)),
                    painter = painterResource(R.drawable.home_image_1),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Image(
                    modifier = Modifier
                        .weight(.5f)
                        .padding(start = margin_5)
                        .clip(shape = RoundedCornerShape(margin_20)),
                    painter = painterResource(R.drawable.home_image_2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontFamily = MarcellusFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 24.sp,
                        letterSpacing = 0.5.sp,
                        color = FurnitureDetailTextColor
                    )) {
                        append(stringResource(R.string.why_is_our_sofa))
                    }
                    withStyle(style = SpanStyle(
                        fontFamily = MarcellusFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 24.sp,
                        letterSpacing = 0.5.sp,
                        color = SelectedRouteBackgroundColor
                    )) {
                        append(stringResource(R.string.best_choice))
                    }
                }
            )

            Row(
                modifier = Modifier
                    .padding(vertical = margin_20)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                viewModel.bestChoiseList.forEach { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(item.imageId),
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(item.textId),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Sofa360Screen(viewModel.images)
        }

        Text(
            text = stringResource(R.string.your_comfort_in_a_few_steps),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = margin_10),
            style = MaterialTheme.typography.titleMedium
        )

        StepListScreen(viewModel.stepList)

        AboutUsScreen(
            viewModel::openYoutube,
            viewModel::openWhatsUp,
            viewModel::openInstagram,
            viewModel::openTikTok,
        )
    }
}

@Composable
fun Sofa360Screen(images: List<Int>) {
    val margin_20 = dimensionResource(R.dimen.margin_20)
    val pageCount = 36

    Box(
        contentAlignment = Alignment.BottomCenter
    ) {
        val pagerState = rememberPagerState(pageCount = { pageCount })
        val coroutineScope = rememberCoroutineScope()

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .scrollable(
                    rememberScrollableState { delta ->
                        coroutineScope.launch {
                            if (delta < 0) {
                                if (pagerState.currentPage == 0) {
                                    pagerState.scrollToPage(pageCount - 1)
                                } else {
                                    pagerState.scrollToPage(pagerState.currentPage - 1)
                                }
                            } else {
                                pagerState.scrollToPage((pagerState.currentPage + 1) % pageCount)
                            }
                        }
                        delta
                    },
                    orientation = Orientation.Horizontal
            ),
            beyondViewportPageCount = pageCount,
            state = pagerState,
            userScrollEnabled = false,
        ) { page ->

            Image(
                painter = painterResource(images[page]),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(margin_20)),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Image(
            painter = painterResource(R.drawable.i_360),
            contentDescription = null
        )
    }
}

@Composable
fun StepListScreen(stepList: List<InfoModel>) {
    val margin_20 = dimensionResource(R.dimen.margin_20)
    val size_50 = dimensionResource(R.dimen.size_50)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .paint(
                painter = painterResource(R.drawable.home_bg_image),
                contentScale = ContentScale.Crop
            )
            .padding(top = margin_20),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stepList.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .padding(vertical = margin_20),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(item.imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = margin_20)
                        .size(size_50)
                )
                Text(
                    text = stringResource(item.textId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AboutUsScreen(
    openYoutube: () -> Unit,
    openWhatsUp: () -> Unit,
    openInstagram: () -> Unit,
    openTikTok: () -> Unit,
) {
    val margin_10 = dimensionResource(R.dimen.margin_10)
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    Column(
        modifier = Modifier
            .background(AboutUsBackgroundColor)
            .padding(margin_16)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.sofantastica_title),
            style = MaterialTheme.typography.labelMedium,
            color = TitleColor,
            modifier = Modifier.padding(bottom = margin_20)
        )

        Text(
            text = stringResource(R.string.address),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = margin_10)
                .clickable { }
        )

        Text(
            modifier = Modifier
                .padding(vertical = margin_10),
            text =
                buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontFamily = MondaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        color = AboutUsTextColor
                    )) {
                        append(stringResource(R.string.call_us))
                    }
                    withStyle(style = SpanStyle(
                        fontFamily = MondaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        color = Color.White
                    )) {
                        append(stringResource(R.string.phone_number))
                    }
                }
        )

        Text(
            modifier = Modifier
                .padding(vertical = margin_10),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    fontFamily = MondaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.5.sp,
                    color = AboutUsTextColor
                )) {
                    append(stringResource(R.string.contact_us))
                }
                withStyle(style = SpanStyle(
                    fontFamily = MondaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.5.sp,
                    color = Color.White
                )) {
                    append(stringResource(R.string.email))
                }
            }
        )

        Row(modifier = Modifier.padding(top = margin_20, bottom = margin_10)) {
            Image(
                painter = painterResource(R.drawable.i_youtube),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = margin_20)
                    .clickable {
                        openYoutube()
                    }
            )
            Image(
                painter = painterResource(R.drawable.i_whatsup),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = margin_20)
                    .clickable {
                        openWhatsUp()
                    }
            )
            Image(
                painter = painterResource(R.drawable.i_instagram),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = margin_20)
                    .clickable {
                        openInstagram()
                    }
            )
            Image(
                painter = painterResource(R.drawable.i_tiktok),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = margin_20)
                    .clickable {
                        openTikTok()
                    }
            )
        }
    }
}