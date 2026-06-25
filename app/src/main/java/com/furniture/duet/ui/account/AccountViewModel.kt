package com.furniture.duet.ui.account

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.furniture.duet.background.InternetConnectionManager
import com.furniture.duet.data.model.account.AccountModel
import com.furniture.duet.data.model.order.OrderHistoryModel
import com.furniture.duet.domain.exceptions.ResIdException
import com.furniture.duet.domain.usecase.account.GetUserDataUseCase
import com.furniture.duet.domain.usecase.account.LogOutUseCase
import com.furniture.duet.domain.usecase.account.UpdateUserUseCase
import com.furniture.duet.ui.common.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    private val _getUser: GetUserDataUseCase,
    private val _updateUser: UpdateUserUseCase,
    private val _logOut: LogOutUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var isEditable by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
    var fullName by mutableStateOf("")
        private set
    var phone by mutableStateOf("")
        private set
    var address by mutableStateOf("")
        private set
    var country by mutableStateOf("")
        private set
    var city by mutableStateOf("")
        private set
    var postCode by mutableStateOf("")
        private set

    var isBusiness by mutableStateOf(false)
    var unn by mutableStateOf("")
        private set
    var companyName by mutableStateOf("")
        private set

    var uiState by mutableStateOf<UiState<Unit>>(UiState.Loading)
        private set

    init {
        getUserData()
    }

    fun getUserData() {
        try {
            viewModelScope.launch {
                uiState = UiState.Loading
                _getUser()?.let {
                    email = it.email
                    fullName = it.firstAndLastName
                    address = it.address
                    country = it.country
                    city = it.city
                    postCode = it.postCode
                    unn = it.unn
                    companyName = it.companyName
                    uiState = UiState.Success(Unit)
                }
            }
        } catch (e: ResIdException) {
            Toast.makeText(context, e.resId, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            uiState = UiState.Error(e)
        }
    }

    fun editProfile() {
        isEditable = true
    }

    fun saveProfile() {
        isEditable = false
        viewModelScope.launch {
            _updateUser(
                firstAndLastName = fullName,
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
                postCode = newPostalCode
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

    fun setNewUNN(newUNN: String) {
        viewModelScope.launch {
            if (newUNN.matches("\\d*".toRegex())) {
                unn = newUNN
            }
        }
    }

    fun setNewCompanyName(newCompanyName: String) {
        viewModelScope.launch {
            companyName = newCompanyName
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _logOut()
            email = ""
            fullName = ""
            address = ""
            country = ""
            city = ""
            postCode = ""
            unn = ""
            companyName = ""
            uiState = UiState.Loading
        }
    }

}