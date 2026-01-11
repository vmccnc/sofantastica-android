package pl.sofantastica.domain.usecase.account

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val auth: FirebaseAuth
) {
    operator fun invoke() = auth.currentUser
}