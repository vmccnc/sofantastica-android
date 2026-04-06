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
    private val connectionManager: InternetConnectionManager,
    private val _getUser: GetUserDataUseCase,
    private val _updateUser: UpdateUserUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var isEditable by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set
    var fullName by mutableStateOf("")
    var phone by mutableStateOf("")
    var address by mutableStateOf("")
    var country by mutableStateOf("")
    var city by mutableStateOf("")
    var postCode by mutableStateOf("")

    var uiState by mutableStateOf<UiState<Unit>>(UiState.Loading)
        private set

    init {
        getUserData()
    }

    fun getUserData() {
        try {
            connectionManager.isOnline()
            viewModelScope.launch {
                _getUser()?.let {
                    email = it.email
                    fullName = it.firstAndLastName
                    address = it.address
                    country = it.country
                    city = it.city
                    postCode = it.postCode
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
                customerType = "",
                firstAndLastName = fullName,
                companyName = "",
                unn = "",
                phone = phone,
                email = email,
                address = address,
                city = city,
                country = country,
                postCode = postCode
            )
        }
    }

    fun setNewPhone(newPhone: String) {
        viewModelScope.launch {
            if (Pattern.compile("\\p{Sm}?\\d*").matcher(newPhone).matches()) {
                phone = newPhone
            }
        }
    }

}