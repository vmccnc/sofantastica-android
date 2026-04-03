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
import com.furniture.duet.domain.usecase.order.CreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val _createOrder: CreateOrderUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {
    var isOrderSent by mutableStateOf(false)
        private set

    var isBusiness by mutableStateOf(false)

    var unn by mutableStateOf("")
        private set

    fun setNewUNN(newUNN: String) {
        viewModelScope.launch {
            unn = newUNN
        }
    }

    var fullName by mutableStateOf("")
        private set

    fun setNewFullName(newFullName: String) {
        viewModelScope.launch {
            fullName = newFullName
        }
    }

    var city by mutableStateOf("")
        private set

    fun setNewCity(newCity: String) {
        viewModelScope.launch {
            city = newCity
        }
    }

    var street by mutableStateOf("")
        private set

    fun setNewStreet(newStreet: String) {
        viewModelScope.launch {
            street = newStreet
        }
    }

    var house by mutableStateOf("")
        private set

    fun setNewHouse(newHouse: String) {
        viewModelScope.launch {
            if(Pattern.compile("\\d*").matcher(newHouse).matches()) {
                house = newHouse
            }
        }
    }

    var postalCode by mutableStateOf("")
        private set

    fun setNewPostalCode(newPostalCode: String) {
        viewModelScope.launch {
            postalCode = newPostalCode
        }
    }

    var email by mutableStateOf("")
        private set

    fun setNewEmail(newEmail: String) {
        viewModelScope.launch {
            email = newEmail
        }
    }

    var phone by mutableStateOf("")
        private set

    fun setNewPhone(newPhone: String) {
        viewModelScope.launch {
            if (newPhone.isEmpty() || Pattern.compile("\\p{Sm}?\\d{0,12}").matcher(newPhone).matches()) {
                phone = newPhone
            }
        }
    }

    var country by mutableStateOf("")
        private set

    fun setNewCountry(newCountry: String) {
        viewModelScope.launch {
            country = newCountry
        }
    }

    var companyName by mutableStateOf("")
        private set

    fun setNewCompanyName(newCompanyName: String) {
        viewModelScope.launch {
            companyName = newCompanyName
        }
    }

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

    fun createOrder() {
        viewModelScope.launch {
            try {
                if(_createOrder(isBusiness, fullName,
                    companyName, unn, email, phone,
                    "$street $house", city, postalCode, country,
                    selectedDeliveryOption?.text ?: "",
                    selectedPaymentOption?.text ?: ""
                )) {
                    isOrderSent = true
                }
            } catch (e: ResIdException) {
                Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
            }
        }
    }
}