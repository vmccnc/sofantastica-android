package pl.sofantastica.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import pl.sofantastica.background.InternetConnectionManager
import pl.sofantastica.data.repository.CartRepository
import pl.sofantastica.data.repository.FavoritesRepository
import pl.sofantastica.domain.exceptions.WrongLoginOrPasswordException
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val favoriteRepository: FavoritesRepository,
    private val cartRepository: CartRepository,
    private val connectionManager: InternetConnectionManager
) {
    suspend operator fun invoke(email: String, password: String) {
        connectionManager.isOnline()
        if (!email.endsWith("@gmail.com") || password.isEmpty()) {
            throw WrongLoginOrPasswordException()
        }
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        firebaseAuth.currentUser ?: throw WrongLoginOrPasswordException()

        favoriteRepository.loadFavorite()
        cartRepository.loadCart()
    }
}
