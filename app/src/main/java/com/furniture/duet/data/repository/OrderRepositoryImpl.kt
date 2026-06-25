package com.furniture.duet.data.repository

import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.api.RetrofitApiService
import com.furniture.duet.data.db.dao.CartDao
import com.furniture.duet.data.model.order.CreateOrderModel
import com.furniture.duet.data.model.order.CustomerType
import com.furniture.duet.data.model.order.DeliveryMethodType
import com.furniture.duet.data.model.order.OrderHistoryModel
import com.furniture.duet.data.model.order.OrderModel
import com.furniture.duet.data.model.order.OrderStatus
import com.furniture.duet.data.model.order.PaymentType
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class OrderRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val connectionManager: InternetConnectionManager,
    private val api: RetrofitApiService,
    private val cartDao: CartDao
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
        typeOfDelivery: Int,
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
            typeOfPayment = typeOfPayment
        )

        val response = api.createOrder(order)

        cartDao.deleteAll()

        return response.isSuccessful
    }

    override suspend fun getOrders(page: Int) = withContext(Dispatchers.IO) {
        connectionManager.isOnline()
        val user = auth.currentUser ?: throw IsNotAuthorizeException()
        val response = api.listOrders(user.uid, page, 1)

        if (!response.isSuccessful || response.body() == null)
            return@withContext OrderHistoryModel(
                orders = emptyList(),
                page = page,
                isLast = true
            )
        val result = response.body()!!
        OrderHistoryModel(
            orders = result.orders.map {
                OrderModel(
                    id = it.id,
                    userId = it.userId,
                    customerType = CustomerType.valueOf(it.customerType),
                    firstAndLastName = it.firstAndLastName,
                    companyName = it.companyName,
                    unn = it.unn,
                    email = it.email,
                    phone = it.phone,
                    address = it.address,
                    city = it.city,
                    postCode = it.postCode,
                    country = it.country,
                    typeOfDelivery = DeliveryMethodType.valueOf(it.typeOfDelivery),
                    deliveryMethodId = it.deliveryMethodId,
                    deliveryTime = it.deliveryTime,
                    typeOfPayment = PaymentType.valueOf(it.typeOfPayment),
                    orderDate = it.orderDate,
                    status = OrderStatus.valueOf(it.status),
                    deliveryCost = it.deliveryCost,
                    amount = it.amount,
                    items = it.items
                )
            },
            page = page,
            isLast = result.totalPages == page
        )
    }
}