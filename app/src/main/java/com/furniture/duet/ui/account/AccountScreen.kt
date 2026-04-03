package com.furniture.duet.ui.account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.furniture.duet.R
import com.furniture.duet.ui.auth.LoginRoute
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.auth.LoginScreen
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.orders.OrderHistoryScreen
import com.furniture.duet.ui.orders.StyledTextField
import com.furniture.duet.ui.theme.EnabledBtnColor
import com.furniture.duet.ui.theme.SearchBarBackgroundColor

@Composable
fun AccountRoute(
    goBack: () -> Unit,
    //goToLogin: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    if (viewModel.email.isEmpty()) {
        LoginRoute(goBack)
        return
        //goToLogin()
    }
    val state = viewModel.uiState
    if (state is UiState.Error) {
        ErrorUI("Error: ${state.throwable.message}")
    } else {
        var accountMode by remember{ mutableStateOf(false) }
        val margin_16 = dimensionResource(R.dimen.margin_16)
        val margin_5 = dimensionResource(R.dimen.margin_5)
        val margin_20 = dimensionResource(R.dimen.margin_20)
        Column {
            Text(text = stringResource(R.string.account_label),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = margin_5)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                var textColorAcc = EnabledBtnColor
                var backgroundColorAcc = Color.White
                var textColorShop = Color.White
                var backgroundColorShop = EnabledBtnColor

                if (accountMode) {
                    textColorAcc = Color.White
                    backgroundColorAcc = EnabledBtnColor
                    textColorShop = EnabledBtnColor
                    backgroundColorShop = Color.White
                }

                Text(
                    text = stringResource(R.string.your_account),
                    style = MaterialTheme.typography.labelSmall,
                    color = textColorAcc,
                    modifier = Modifier
                        .background(backgroundColorAcc, RoundedCornerShape(margin_20))
                        .padding(vertical = margin_16, horizontal = margin_20)
                        .clickable { accountMode = true }
                )
                Text(
                    text = stringResource(R.string.shopping),
                    style = MaterialTheme.typography.labelSmall,
                    color = textColorShop,
                    modifier = Modifier
                        .background(backgroundColorShop, RoundedCornerShape(margin_20))
                        .padding(vertical = margin_16, horizontal = margin_20)
                        .clickable { accountMode = false }
                )
            }
            if(accountMode)
                AccountScreen(goBack)
            else
                OrderHistoryScreen()
        }
    }
}

@Composable
fun AccountScreen(
    goBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val margin_5 = dimensionResource(R.dimen.margin_5)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    val roundedShape = RoundedCornerShape(margin_20)

    val textColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        disabledTextColor = EnabledBtnColor
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(dimensionResource(R.dimen.margin_16))
        .verticalScroll(rememberScrollState())) {
        TextField(value = viewModel.email,
            onValueChange = {},
            label = {Text(stringResource(R.string.email_label))},
            enabled = false,
            singleLine = true,
            colors = textColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = margin_5)
                .clip(roundedShape)
        )
        TextField(value = viewModel.fullName,
            onValueChange = { viewModel.fullName = it },
            label = {Text(stringResource(R.string.full_name_label))},
            enabled = viewModel.isEditable,
            singleLine = true,
            colors = textColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = margin_5)
                .clip(roundedShape)
        )
        TextField(value = viewModel.phone,
            onValueChange = viewModel::setNewPhone,
            label = {Text(stringResource(R.string.phone_number_label))},
            enabled = viewModel.isEditable,
            singleLine = true,
            colors = textColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = margin_5)
                .clip(roundedShape)
        )
        TextField(value = viewModel.address,
            onValueChange = { viewModel.address = it },
            label = {Text(stringResource(R.string.address_label))},
            enabled = viewModel.isEditable,
            singleLine = true,
            colors = textColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = margin_5)
                .clip(roundedShape)
        )
        TextField(value = viewModel.country,
            onValueChange = { viewModel.country = it },
            label = {Text(stringResource(R.string.country_label))},
            enabled = viewModel.isEditable,
            singleLine = true,
            colors = textColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = margin_5)
                .clip(roundedShape)
        )
        TextField(value = viewModel.city,
            onValueChange = { viewModel.city = it },
            label = {Text(stringResource(R.string.city_label))},
            enabled = viewModel.isEditable,
            singleLine = true,
            colors = textColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = margin_5)
                .clip(roundedShape)
        )
        TextField(value = viewModel.postCode,
            onValueChange = { viewModel.postCode = it },
            label = {Text(stringResource(R.string.post_code_label))},
            enabled = viewModel.isEditable,
            singleLine = true,
            colors = textColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = margin_5)
                .clip(roundedShape)
        )
        if (viewModel.isEditable) {
            Button(
                modifier = Modifier
                    .padding(vertical = margin_5)
                    .fillMaxWidth(),
                onClick = { viewModel.saveProfile() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = EnabledBtnColor
                )
            ) {
                Text(
                    text = stringResource(R.string.save_profile),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        } else {
            Button(
                modifier = Modifier
                    .padding(vertical = margin_5)
                    .fillMaxWidth(),
                onClick = { viewModel.editProfile() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = EnabledBtnColor
                )
            ) {
                Text(
                    text = stringResource(R.string.edit_profile),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        Button(
            modifier = Modifier
                .padding(vertical = margin_5)
                .fillMaxWidth(),
            onClick = { viewModel.logOut() },
            colors = ButtonDefaults.buttonColors(
                containerColor = EnabledBtnColor,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.sign_out),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}