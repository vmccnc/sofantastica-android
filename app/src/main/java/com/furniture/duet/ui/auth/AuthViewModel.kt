package com.furniture.duet.ui.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.domain.usecase.account.ResetPasswordUesCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.furniture.duet.domain.usecase.account.SignInUseCase
import com.furniture.duet.domain.usecase.account.SignUpUseCase
import com.furniture.duet.ui.common.UiState
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val _signInUseCase: SignInUseCase,
    private val _signUpUseCase: SignUpUseCase,
    private val resetPasswordUseCase: ResetPasswordUesCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private companion object {
        const val EMPTY_STRING = ""
    }

    var email by mutableStateOf(EMPTY_STRING)
        private set
    var password by mutableStateOf(EMPTY_STRING)
        private set
    var phoneNumber by mutableStateOf(EMPTY_STRING)
        private set
    var fullName by mutableStateOf(EMPTY_STRING)
        private set

    var uiState by mutableStateOf<UiState<Unit>>(UiState.Loading)
        private set

    var isPasswordHidden by mutableStateOf(true)
        private set

    var isLogin by mutableStateOf(true)
    var isForgotPassword by mutableStateOf(false)

    var isPrivacyPolicyChecked by mutableStateOf(false)

    fun signIn() {
        viewModelScope.launch {
            try {
                _signInUseCase(email, password)
                uiState = UiState.Success(Unit)
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            try {
                if (
                    isPrivacyPolicyChecked &&
                    email.isNotEmpty() && email.endsWith("@gmail.com") &&
                    phoneNumber.isNotEmpty() &&
                    fullName.isNotEmpty() &&
                    phoneNumber.isNotEmpty() &&
                    Pattern.compile("\\p{Sm}\\d{10,12}").matcher(phoneNumber).matches() &&
                    password.length >= 8 &&
                    Pattern.compile(".*[A-Z]+.*").matcher(password).matches() &&
                    Pattern.compile(".*[^A-Za-z0-9]+.*").matcher(password).matches() &&
                    Pattern.compile(".*\\d+.*").matcher(password).matches()
                ) {
                    _signUpUseCase(
                        fullName,
                        phoneNumber,
                        email,
                        password
                    )
                    uiState = UiState.Success(Unit)
                } else {
                    Toast.makeText(context, "Check your data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun resetPassword() {
        viewModelScope.launch {
            resetPasswordUseCase(email)
        }
    }

    fun setNewEmail(newEmail: String) {
        viewModelScope.launch {
            email = newEmail
        }
    }

    fun setNewPassword(newPassword: String) {
        viewModelScope.launch {
            password = newPassword
        }
    }

    fun togglePasswordHidden() {
        viewModelScope.launch {
            isPasswordHidden = !isPasswordHidden
        }
    }

    fun setNewFullName(newFullName: String) {
        viewModelScope.launch {
            fullName = newFullName
        }
    }

    fun setNewPhoneNumber(newPhoneNumber: String) {
        viewModelScope.launch {
            if (newPhoneNumber.isEmpty() ||
                Pattern.compile("\\p{Sm}?\\d*").matcher(newPhoneNumber).matches()) {
                phoneNumber = newPhoneNumber
            }
        }
    }
}
