package pl.sofantastica.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        try {
            if (email.endsWith("@gmail.com") && password.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            }
        } catch (_: Exception) {
        }
        return firebaseAuth.currentUser != null
    }
}
