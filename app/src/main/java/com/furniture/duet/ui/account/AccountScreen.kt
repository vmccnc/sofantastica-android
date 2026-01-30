package com.furniture.duet.ui.account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.furniture.duet.R
import com.furniture.duet.ui.ErrorUI
import com.furniture.duet.ui.auth.LoginScreen
import com.furniture.duet.ui.common.UiState

@Composable
fun AccountRoute(
    goBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    if (state is UiState.Error) {
        ErrorUI("Error: ${state.throwable.message}")
    } else {
        AccountScreen(goBack)
    }
}

@Composable
fun AccountScreen(
    goBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {

    if (viewModel.getUserData() == null) {
        //goToLogInPage()
        LoginScreen(goBack)
        return
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.setPhotoUrl(it)
            }
        }

    val textColors = TextFieldDefaults.colors(
        disabledTextColor = Color.DarkGray,
        disabledLabelColor = Color.Gray,
        disabledContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent
    )

    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val userPhotoUri by viewModel.userPhotoUri.collectAsState()
    val isEditable by viewModel.isEditable.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(dimensionResource(R.dimen.margin_16))) {
        Row {
            AsyncImage(
                model = userPhotoUri,
                contentDescription = null,
                modifier = Modifier
                    .height(dimensionResource(R.dimen.size_128))
                    .width(dimensionResource(R.dimen.size_128)),
                error = painterResource(R.drawable.blank_user)
            )
            if(isEditable) {
                TextButton(onClick = {
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Upload Image")
                }
                IconButton(onClick = viewModel::saveProfile) {
                    Icon(Icons.Default.Check, contentDescription = null)
                }
            } else {
                IconButton(onClick = viewModel::editProfile) {
                    Icon(Icons.Default.Create, contentDescription = null)
                }
            }
        }
        TextField(value = userEmail,
            onValueChange = {},
            label = {Text("Email")},
            enabled = false,
            colors = textColors
        )
        TextField(value = userName,
            onValueChange = viewModel::setName,
            label = {Text("Name")},
            enabled = isEditable,
            colors = textColors
        )
        TextButton(onClick = {
            viewModel.logOut()
            goBack()
            //goBack()
        }) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Text("Sign Out")
        }
    }
}