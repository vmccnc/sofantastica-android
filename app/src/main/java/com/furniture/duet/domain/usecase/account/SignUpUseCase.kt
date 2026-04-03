package com.furniture.duet.domain.usecase.account

import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.data.repository.UserRepository
import com.furniture.duet.data.repository.UserRepositoryImpl
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
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String
    ) {
        userRepository.createAccount(
            email = email,
            firstAndLastName = fullName,
            phoneNumber = phoneNumber,
            password = password
        )
    }
}
