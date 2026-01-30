package com.furniture.duet.ui.fabrics

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.furniture.duet.domain.usecase.fabrics.GetFabricsUseCase
import javax.inject.Inject

@HiltViewModel
class FabricsViewModel @Inject constructor(
    private val getFabrics: GetFabricsUseCase
) : ViewModel() {

}
