package com.furniture.duet.domain.usecase.account

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.data.repository.UserRepository
import com.furniture.duet.domain.exceptions.WrongEmailFormatException
import com.furniture.duet.domain.exceptions.WrongLoginOrPasswordException
import com.furniture.duet.domain.exceptions.WrongPasswordFormatException
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        if (!email.matches(".+@.+\\..+".toRegex())) {
            throw WrongEmailFormatException()
        }
        userRepository.signIn(email, password)
        cartRepository.syncCart()
        favoritesRepository.loadFavorites()
    }
}
