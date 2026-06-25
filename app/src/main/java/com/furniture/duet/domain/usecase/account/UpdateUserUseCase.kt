package com.furniture.duet.domain.usecase.account

import com.furniture.duet.data.model.account.AccountDto
import com.furniture.duet.data.model.order.CustomerType
import com.furniture.duet.data.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
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
        val customerType =
            if (unn.isNotEmpty()) CustomerType.BUSINESS.name
            else CustomerType.PRIVATE.name

        userRepository.updateAccount(
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
    }
}
