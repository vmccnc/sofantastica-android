package pl.sofantastica.domain.usecase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        try {
            if (email.endsWith("@gmail.com") && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
            }
        } catch (_: Exception) {
        }
        return firebaseAuth.currentUser != null
    }
}
