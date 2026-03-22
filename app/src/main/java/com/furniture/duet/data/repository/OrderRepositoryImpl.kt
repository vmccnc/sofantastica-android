package com.furniture.duet.data.repository

import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.model.order.CreateOrderModel
import com.furniture.duet.data.model.order.GetOrdersModel
import com.furniture.duet.data.model.order.OrderDto
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import com.google.firebase.auth.FirebaseAuth
import retrofit2.HttpException
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager,
    private val api: RetrofitApiService
) : OrderRepository {
    override suspend fun createOrder(
        customerType: String,
        firstAndLastName: String,
        companyName: String,
        unn: String,
        email: String,
        phone: String,
        address: String,
        city: String,
        postCode: String,
        country: String,
        typeOfDelivery: String,
        typeOfPayment: String
    ): Boolean {
        connectionManager.isOnline()
        val user = auth.currentUser ?: throw IsNotAuthorizeException()

        val order = CreateOrderModel(
            userId = user.uid,
            customerType = customerType,
            firstAndLastName = firstAndLastName,
            companyName = companyName,
            unn = unn,
            email = email,
            phone = phone,
            address = address,
            city = city,
            postCode = postCode,
            country = country,
            typeOfDelivery = typeOfDelivery,
            typeOfPayment = typeOfPayment,
            status = "NEW"
        )

        val response = api.createOrder(order)
        return response.isSuccessful
    }

    override suspend fun getOrders(): List<OrderDto> {
        connectionManager.isOnline()
        val response = api.listOrders(auth.currentUser?.uid ?: "")
        if (response.isSuccessful) {
            return response.body()?.orders ?: emptyList()
        }
        throw HttpException(response)
    }
}
