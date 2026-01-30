package com.furniture.duet.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.domain.exceptions.WrongLoginOrPasswordException
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager,
    private val cartRepository: CartRepository,
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        connectionManager.isOnline()
        if (!email.endsWith("@gmail.com") || password.isEmpty()) {
            throw WrongLoginOrPasswordException()
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        firebaseAuth.currentUser ?: throw WrongLoginOrPasswordException()

        cartRepository.loadCart()
        favoritesRepository.loadFavorites()
    }
}
