package pl.sofantastica.ui.auth

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.sofantastica.R
import pl.sofantastica.domain.exceptions.ResIdException
import pl.sofantastica.domain.usecase.SignInUseCase
import pl.sofantastica.domain.usecase.SignUpUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
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
    private val _isSignedIn = MutableStateFlow(firebaseAuth.currentUser != null)
    val isSignedIn = _isSignedIn.asStateFlow()

    fun signIn() {
        viewModelScope.launch {
            try {
                signInUseCase(email.value, password.value)
                _isSignedIn.emit(true)
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            try {
                signUpUseCase(email.value, password.value)
                _isSignedIn.emit(true)
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
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
