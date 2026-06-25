package com.furniture.duet.domain.usecase.account

import android.util.Log
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.domain.exceptions.WrongEmailFormatException
import com.furniture.duet.domain.exceptions.WrongLoginOrPasswordException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ResetPasswordUesCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager
) {
    suspend operator fun invoke(email: String) {
        connectionManager.isOnline()
        if (!email.matches(".+@.+\\..+".toRegex())) {
            throw WrongEmailFormatException()
        }
        firebaseAuth.sendPasswordResetEmail(email).await()
    }
}