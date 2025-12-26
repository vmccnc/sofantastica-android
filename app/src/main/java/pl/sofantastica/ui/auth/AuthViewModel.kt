package pl.sofantastica.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.sofantastica.domain.usecase.SignInUseCase
import pl.sofantastica.domain.usecase.SignUpUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private companion object {
        const val SIGN_IN_ERROR = "Incorrect User Email or Password"
        const val SIGN_UP_ERROR = "Can't create new User. Check your Email and Internet Access"
        const val EMPTY_STRING = ""
    }

    private val _email = MutableStateFlow(EMPTY_STRING)
    val email = _email.asStateFlow()
    private val _password = MutableStateFlow(EMPTY_STRING)
    val password = _password.asStateFlow()
    private val _isSignedIn = MutableStateFlow(firebaseAuth.currentUser != null)
    val isSignedIn = _isSignedIn.asStateFlow()
    private val _errorText = MutableStateFlow(EMPTY_STRING)
    val errorText = _errorText.asStateFlow()

    fun signIn() {
        viewModelScope.launch {
            if (signInUseCase.invoke(email.value, password.value)) {
                _isSignedIn.emit(true)
            } else {
                _errorText.emit(SIGN_IN_ERROR)
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            if (signUpUseCase.invoke(email.value, password.value)) {
                signIn()
            } else {
                _errorText.emit(SIGN_UP_ERROR)
            }
        }
    }

    fun setEmail(newEmail: String) {
        _email.update { newEmail }
    }

    fun setPassword(newPassword: String) {
        _password.update { newPassword }
    }

    fun clearErrorText() {
        if (_errorText.value.isNotEmpty()) {
            _errorText.update { EMPTY_STRING }
        }
    }
}
