package com.furniture.duet.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.furniture.duet.R
import com.furniture.duet.data.model.InfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    val bestChoiseList = listOf(
        InfoModel(R.drawable.i_healtcare, R.string.ultimate_comfort),
        InfoModel(R.drawable.i_piggybank, R.string.budget_friendly),
        InfoModel(R.drawable.i_warranty_badge, R.string.built_to_last)
    )
    val stepList = listOf(
        InfoModel(R.drawable.i_sofa, R.string.choose_a_furniture_model),
        InfoModel(R.drawable.i_thread, R.string.select_upholstery_fabric),
        InfoModel(R.drawable.i_wrench, R.string.add_reinforcement_options),
        InfoModel(R.drawable.i_cart, R.string.place_your_order),
        InfoModel(R.drawable.i_cup, R.string.take_a_coffee_break),
        InfoModel(R.drawable.i_smile, R.string.wait_for_your_furniture_with_a_smile)
    )
    val images = listOf(
        R.drawable.sofa0, R.drawable.sofa1, R.drawable.sofa2,
        R.drawable.sofa3, R.drawable.sofa4, R.drawable.sofa5,
        R.drawable.sofa6, R.drawable.sofa7, R.drawable.sofa8,
        R.drawable.sofa9, R.drawable.sofa10, R.drawable.sofa11,
        R.drawable.sofa12, R.drawable.sofa13, R.drawable.sofa14,
        R.drawable.sofa15, R.drawable.sofa16, R.drawable.sofa17,
        R.drawable.sofa18, R.drawable.sofa19, R.drawable.sofa20,
        R.drawable.sofa21, R.drawable.sofa22, R.drawable.sofa23,
        R.drawable.sofa24, R.drawable.sofa25, R.drawable.sofa26,
        R.drawable.sofa27, R.drawable.sofa28, R.drawable.sofa29,
        R.drawable.sofa30, R.drawable.sofa31, R.drawable.sofa32,
        R.drawable.sofa33, R.drawable.sofa34, R.drawable.sofa35
    )

    fun openWhatsUp() {
        openUri("https://wa.me/451566158")
    }

    fun openInstagram() {
        openUri("https://www.instagram.com/sofa.fantastica/")
    }

    fun openTikTok() {
        openUri("https://www.tiktok.com/@sofa.fantastica")

    }

    private fun openUri(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}