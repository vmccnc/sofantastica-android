package com.furniture.duet.ui.account

import android.net.Uri
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
import com.furniture.duet.domain.usecase.account.GetUserDataUseCase
import com.furniture.duet.domain.usecase.account.LogOutUseCase
import com.furniture.duet.ui.common.UiState
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    private val connectionManager: InternetConnectionManager,
    private val _getUser: GetUserDataUseCase,
    private val _logOut: LogOutUseCase,
    private val _storage: FirebaseStorage
) : ViewModel() {

    private var _user: FirebaseUser? = null

    private var _isEditable = MutableStateFlow(false)
    val isEditable = _isEditable.asStateFlow()

    private var _isUserNameChanged = false
    private var _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()
    private var _userEmail = MutableStateFlow("")
    val userEmail = _userEmail.asStateFlow()
    private var _isUserPhotoUriChanged = false
    private var _userPhotoUri = MutableStateFlow(Uri.EMPTY)
    val userPhotoUri = _userPhotoUri.asStateFlow()

    var uiState by mutableStateOf<UiState<Unit>>(UiState.Success(Unit))
        private set

    fun getUserData(): FirebaseUser? {
        try {
            connectionManager.isOnline()
        } catch (e: Exception) {
            uiState = UiState.Error(e)
        }
        _user = _getUser()
        viewModelScope.launch {
            _userName.update { _user?.displayName ?: "" }
            _userEmail.update { _user?.email ?: "" }
            _userPhotoUri.update { _user?.photoUrl ?: Uri.EMPTY }
        }
        return _user
    }

    fun editProfile() {
        _isEditable.update { true }
    }

    fun saveProfile() {
        try {
            connectionManager.isOnline()
        } catch (e: Exception) {
            uiState = UiState.Error(e)
        }

        val profileChangeRequestBuilder = UserProfileChangeRequest.Builder()

        if (_isUserNameChanged) {
            profileChangeRequestBuilder.setDisplayName(_userName.value)
        }

        if (_isUserPhotoUriChanged) {
            val ref = _storage.reference.child("users/${_user?.uid}/profile.jpg")
            ref.putFile(_userPhotoUri.value).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    val request = profileChangeRequestBuilder.setPhotoUri(it).build()
                    Firebase.auth.currentUser?.updateProfile(request)
                    _userPhotoUri.update { it }
                }
            }
        } else {
            val request = profileChangeRequestBuilder.build()
            Firebase.auth.currentUser?.updateProfile(request)
        }

        _isUserNameChanged = false
        _isUserPhotoUriChanged = false
        _isEditable.update { false }
    }

    fun setName(name: String) {
        _isUserNameChanged = true
        _userName.update { name }
    }

    fun setPhotoUrl(photoUrl: Uri) {
        _isUserPhotoUriChanged = true
        _userPhotoUri.update { photoUrl }
    }


    fun logOut() {
        try {
            connectionManager.isOnline()
        } catch (e: Exception) {
            uiState = UiState.Error(e)
        }
        viewModelScope.launch {
            _logOut()
        }
    }

}