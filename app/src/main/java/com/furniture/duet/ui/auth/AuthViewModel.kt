package com.furniture.duet.ui.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.R
import com.furniture.duet.domain.exceptions.ResIdException
import com.furniture.duet.domain.exceptions.WrongPhoneNumberException
import com.furniture.duet.domain.usecase.account.LogOutUseCase
import com.furniture.duet.domain.usecase.account.ResetPasswordUesCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.furniture.duet.domain.usecase.account.SignInUseCase
import com.furniture.duet.domain.usecase.account.SignUpUseCase
import com.furniture.duet.ui.common.UiState
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val _signInUseCase: SignInUseCase,
    private val _signUpUseCase: SignUpUseCase,
    private val _logOut: LogOutUseCase,
    private val _firebaseAuth: FirebaseAuth,
    private val _resetPasswordUseCase: ResetPasswordUesCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private companion object {
        const val EMPTY_STRING = ""
    }

    var email by mutableStateOf(EMPTY_STRING)
        private set
    var password by mutableStateOf(EMPTY_STRING)
        private set
    var passwordConfirmation by mutableStateOf(EMPTY_STRING)
        private set

    var uiState by mutableStateOf<UiState<Unit>>(UiState.Loading)
        private set

    var isPasswordHidden by mutableStateOf(true)
        private set
    var isPasswordConfirmationHidden by mutableStateOf(true)
        private set

    var isLogin by mutableStateOf(true)
    var isForgotPassword by mutableStateOf(false)

    var isPrivacyPolicyChecked by mutableStateOf(false)

    init {
        if (_firebaseAuth.currentUser != null)
            uiState = UiState.Success(Unit)
    }

    fun signIn() {
        viewModelScope.launch {
            try {
                _signInUseCase(email, password)
                uiState = UiState.Success(Unit)
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            if (!isPrivacyPolicyChecked) {
                Toast.makeText(context, R.string.privacy_policy_not_checked, Toast.LENGTH_SHORT).show()
                return@launch
            }
            try {
                _signUpUseCase(
                    email,
                    password,
                    passwordConfirmation
                )
                uiState = UiState.Success(Unit)
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            uiState = try {
                _logOut()
                UiState.Loading
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }

    fun resetPassword() {
        viewModelScope.launch {
            try {
                _resetPasswordUseCase(email)
                email = ""
                password = ""
                passwordConfirmation = ""
                isForgotPassword = false
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            }  catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun setNewEmail(newEmail: String) {
        viewModelScope.launch {
            email = newEmail
        }
    }

    fun setNewPassword(newPassword: String) {
        viewModelScope.launch {
            if (newPassword.isEmpty() || newPassword.last() != ' ') {
                password = newPassword
            }
        }
    }

    fun togglePasswordHidden() {
        viewModelScope.launch {
            isPasswordHidden = !isPasswordHidden
        }
    }

    fun setNewPasswordConfirmation(newPasswordConfirmation: String) {
        viewModelScope.launch {
            if (newPasswordConfirmation.isEmpty() || newPasswordConfirmation.last() != ' ') {
                passwordConfirmation = newPasswordConfirmation
            }
        }
    }

    fun togglePasswordConfirmationHidden() {
        viewModelScope.launch {
            isPasswordConfirmationHidden = !isPasswordConfirmationHidden
        }
    }
}
