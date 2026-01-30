package com.furniture.duet.domain.usecase.account

import com.google.firebase.auth.FirebaseAuth
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.data.repository.FavoritesRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val cartRepository: CartRepository,
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke() {
        firebaseAuth.signOut()
        cartRepository.clear()
        favoritesRepository.clear()
    }
}
