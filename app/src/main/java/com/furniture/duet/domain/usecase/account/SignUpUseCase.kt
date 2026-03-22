package com.furniture.duet.domain.usecase.account

import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.domain.exceptions.WrongLoginOrPasswordException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.number


class SignUpUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager,
    private val cartRepository: CartRepository,
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String
    ) {
        connectionManager.isOnline()
        if (!email.endsWith("@gmail.com")) {
            throw WrongLoginOrPasswordException()
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        firebaseAuth.currentUser ?: throw WrongLoginOrPasswordException()

        val request = UserProfileChangeRequest.Builder()
            .setDisplayName(fullName)
            .build()
        firebaseAuth.currentUser?.updateProfile(request)

        cartRepository.loadCart()
        favoritesRepository.loadFavorites()
    }
}
