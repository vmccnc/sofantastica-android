package com.furniture.duet.domain.usecase.account

import com.furniture.duet.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getUserData()
}