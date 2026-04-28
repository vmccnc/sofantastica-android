package com.furniture.duet.ui.orders

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furniture.duet.R
import com.furniture.duet.data.model.order.CreateOrderModel
import com.furniture.duet.data.model.order.DeliveryOptionModel
import com.furniture.duet.data.model.order.PaymentOptionModel
import com.furniture.duet.domain.exceptions.IsNotAuthorizeException
import com.furniture.duet.domain.exceptions.ResIdException
import com.furniture.duet.domain.usecase.account.GetUserDataUseCase
import com.furniture.duet.domain.usecase.order.CreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val _createOrder: CreateOrderUseCase,
    private val _getUserInfo: GetUserDataUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {
    var isOrderSent by mutableStateOf(false)
        private set

    var isBusiness by mutableStateOf(false)
    var unn by mutableStateOf("")
        private set
    var companyName by mutableStateOf("")
        private set

    var fullName by mutableStateOf("")
        private set
    var city by mutableStateOf("")
        private set
    var address by mutableStateOf("")
        private set
    var postalCode by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var phone by mutableStateOf("")
        private set
    var country by mutableStateOf("")
        private set

    val deliveryOptionList = listOf(
        DeliveryOptionModel(context.getString(R.string.delivery_without_deposit), 100),
        DeliveryOptionModel(context.getString(R.string.delivery_with_deposit), 150),
        DeliveryOptionModel(context.getString(R.string.delivery_with_deposit_at_the_selected_time), 180)
    )

    var selectedDeliveryOption by mutableStateOf<DeliveryOptionModel?>(null)
        private set

    fun selectDeliveryOption(newSelectedOption: DeliveryOptionModel) {
        selectedDeliveryOption = newSelectedOption
        if (deliveryOptionList[0] == newSelectedOption) {
            isCashPaymentOptionEnabled = false
            if (selectedPaymentOption == cashPaymentOption)
                selectedPaymentOption = null
        } else {
            isCashPaymentOptionEnabled = true
        }
    }

    val paymentOptionList = listOf(
        PaymentOptionModel(context.getString(R.string.payment_card), R.drawable.i_payment_card),
        //PaymentOptionModel(context.getString(R.string.blik), R.drawable.i_blink),
        //PaymentOptionModel(context.getString(R.string.fast_transfer), R.drawable.i_przelewy),
    )

    var isCashPaymentOptionEnabled by mutableStateOf(true)
        private set
    val cashPaymentOption = PaymentOptionModel(context.getString(R.string.cash_on_delivery), R.drawable.i_cash)

    var selectedPaymentOption by mutableStateOf<PaymentOptionModel?>(null)
        private set

    fun selectPaymentOption(newSelectedPaymentOption: PaymentOptionModel) {
        selectedPaymentOption = newSelectedPaymentOption
    }

    init {
        viewModelScope.launch {
            _getUserInfo()?.let {
                email = it.email
                fullName = it.firstAndLastName
                companyName = it.companyName
                unn = it.unn
                phone = it.phone
                address = it.address
                country = it.country
                city = it.city
                postalCode = it.postCode
                isBusiness = unn.isNotEmpty()
            }
        }
    }
    fun createOrder() {
        viewModelScope.launch {
            try {
                isOrderSent = _createOrder(isBusiness, fullName,
                    companyName, unn, email, phone,
                    address, city, postalCode, country,
                    selectedDeliveryOption?.text ?: "",
                    selectedPaymentOption?.text ?: ""
                )
            } catch (e: IllegalArgumentException) {
                Toast.makeText(context, R.string.check_you_re_data, Toast.LENGTH_SHORT).show()
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setNewUNN(newUNN: String) {
        viewModelScope.launch {
            if (newUNN.matches("\\d*".toRegex())) {
                unn = newUNN
            }
        }
    }

    fun setNewFullName(newFullName: String) {
        viewModelScope.launch {
            if (newFullName.matches("[\\w\\s-]{0,256}".toRegex())) {
                fullName = newFullName
            }
        }
    }

    fun setNewPhone(newPhone: String) {
        viewModelScope.launch {
            if (newPhone.isEmpty() || newPhone.matches("[\\p{Sm}8]\\d*".toRegex())) {
                phone = newPhone
            }
        }
    }

    fun setNewPostalCode(newPostalCode: String) {
        viewModelScope.launch {
            if (newPostalCode.matches("\\d*".toRegex())) {
                postalCode = newPostalCode
            }
        }
    }

    fun setNewCity(newCity: String) {
        viewModelScope.launch {
            if (newCity.matches("[\\w\\s-]{0,256}".toRegex())) {
                city = newCity
            }
        }
    }

    fun setNewCountry(newCountry: String) {
        viewModelScope.launch {
            if (newCountry.matches("[\\w\\s-]{0,256}".toRegex())) {
                country = newCountry
            }
        }
    }

    fun setNewAddress(newAddress: String) {
        viewModelScope.launch {
            if (newAddress.matches("[\\w\\s-,.]{0,256}".toRegex())) {
                address = newAddress
            }
        }
    }

    fun setNewEmail(newEmail: String) {
        viewModelScope.launch {
            email = newEmail
        }
    }

    fun setNewCompanyName(newCompanyName: String) {
        viewModelScope.launch {
            companyName = newCompanyName
        }
    }
}