package com.furniture.duet.domain.usecase.account

import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.data.repository.UserRepository
import com.furniture.duet.data.repository.UserRepositoryImpl
import com.furniture.duet.domain.exceptions.EmptyFieldException
import com.furniture.duet.domain.exceptions.PasswordRepeatException
import com.furniture.duet.domain.exceptions.WrongEmailFormatException
import com.furniture.duet.domain.exceptions.WrongLoginOrPasswordException
import com.furniture.duet.domain.exceptions.WrongPasswordFormatException
import com.furniture.duet.domain.exceptions.WrongPhoneNumberException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.number


class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        if (!email.matches(".+@.+\\..+".toRegex())) {
            throw WrongEmailFormatException()
        }
        if (password.length < 8 ||
            !password.matches(".*[A-Z]+.*".toRegex()) ||
            !password.matches(".*[^A-Za-z0-9]+.*".toRegex()) ||
            !password.matches(".*\\d+.*".toRegex())
        ) {
            throw WrongPasswordFormatException()
        }

        if (password != passwordConfirmation)
            throw PasswordRepeatException()

        userRepository.createAccount(
            email = email,
            password = password
        )
    }
}
