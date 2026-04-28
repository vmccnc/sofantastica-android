package com.furniture.duet.domain.usecase.loading

import android.content.Context
import android.widget.Toast
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.data.repository.FabricRepository
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.data.repository.FurnitureRepository
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import com.furniture.duet.domain.exceptions.ResIdException
import com.furniture.duet.ui.common.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val favoritesRepository: FavoritesRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke() {
        try {
            favoritesRepository.loadFavorites()
            cartRepository.loadCart()
        }  catch (e: ResIdException) {
            Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
