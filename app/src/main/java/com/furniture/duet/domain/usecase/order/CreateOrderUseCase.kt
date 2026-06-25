package com.furniture.duet.domain.usecase.order

import com.furniture.duet.data.model.order.DeliveryMethodType
import com.furniture.duet.data.model.order.PaymentType
import com.furniture.duet.data.repository.OrderRepository
import com.furniture.duet.domain.exceptions.EmptyFieldException
import com.furniture.duet.domain.exceptions.WrongEmailFormatException
import com.furniture.duet.domain.exceptions.WrongPhoneNumberException
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(
        isBusiness: Boolean,
        firstAndLastName: String,
        companyName: String,
        unn: String,
        email: String,
        phone: String,
        address: String,
        city: String,
        postCode: String,
        country: String,
        typeOfDelivery: DeliveryMethodType?,
        typeOfPayment: PaymentType?
    ): Boolean {

        if (
            firstAndLastName.isEmpty() ||
            (companyName.isEmpty() || unn.isEmpty()) && isBusiness ||
            email.isEmpty() || phone.isEmpty() ||
            typeOfDelivery == null || typeOfPayment == null ||
            (address.isEmpty() || city.isEmpty() || postCode.isEmpty() || country.isEmpty()) && typeOfDelivery != DeliveryMethodType.Pickup
        ) throw EmptyFieldException()

        if (!email.matches(Regex.fromLiteral(".+@.+\\..+")))
            throw WrongEmailFormatException()
        if (!phone.matches(Regex.fromLiteral("[\\+8]\\d{10,12}")))
            throw WrongPhoneNumberException()

        val customerType =
            if (isBusiness) "business"
            else "private"

        return repository.createOrder(customerType, firstAndLastName,
            companyName, unn, email, phone, address, city,
            postCode, country, typeOfDelivery.id, typeOfPayment.name
        )
    }
}
