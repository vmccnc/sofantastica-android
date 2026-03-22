package com.furniture.duet.ui.home

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.furniture.duet.R
import com.furniture.duet.data.model.InfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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
    val sofa360Link =
        "https://firebasestorage.googleapis.com/v0/b/furniture-dm.firebasestorage.app/o/360%2F"
}