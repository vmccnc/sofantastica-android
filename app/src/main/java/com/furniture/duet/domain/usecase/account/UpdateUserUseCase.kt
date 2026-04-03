package com.furniture.duet.domain.usecase.account

import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.model.account.AccountModel
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.data.repository.UserRepository
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import com.furniture.duet.domain.exceptions.WrongLoginOrPasswordException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val auth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        customerType: String,
        firstAndLastName: String,
        companyName: String,
        unn: String,
        phone: String,
        email: String,
        address: String,
        city: String,
        country: String,
        postCode: String
    ) {
        connectionManager.isOnline()
        auth.currentUser ?: throw IsNotAuthorizeException()

        userRepository.updateAccount(
            AccountModel(
                userId = auth.currentUser?.uid ?: "",
                customerType = customerType,
                firstAndLastName = firstAndLastName,
                companyName = companyName,
                unn = unn,
                phone = phone,
                email = email,
                address = address,
                city = city,
                country = country,
                postCode = postCode
            )
        )
    }
}
