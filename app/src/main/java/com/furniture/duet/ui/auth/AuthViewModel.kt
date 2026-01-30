package com.furniture.duet.ui.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.furniture.duet.domain.usecase.SignInUseCase
import com.furniture.duet.domain.usecase.SignUpUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private companion object {
        const val EMPTY_STRING = ""
    }

    private val _email = MutableStateFlow(EMPTY_STRING)
    val email = _email.asStateFlow()
    private val _password = MutableStateFlow(EMPTY_STRING)
    val password = _password.asStateFlow()

    var uiState by mutableStateOf<UiState<Unit>>(UiState.Loading)
        private set

    fun signIn() {
        viewModelScope.launch {
            try {
                signInUseCase(email.value, password.value)
                uiState = UiState.Success(Unit)
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            try {
                signUpUseCase(email.value, password.value)
                uiState = UiState.Success(Unit)
            } catch (e: Exception) {
                uiState = UiState.Error(e)
            }
        }
    }

    fun setEmail(newEmail: String) {
        _email.update { newEmail }
    }

    fun setPassword(newPassword: String) {
        _password.update { newPassword }
    }
}
