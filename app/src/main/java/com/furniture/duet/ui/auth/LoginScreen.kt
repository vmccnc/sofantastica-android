package com.furniture.duet.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.furniture.duet.R
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.theme.LoginBtnColor
import com.furniture.duet.ui.theme.TitleColor

@Composable
fun LoginRoute(
    goBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    when(state) {
        is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
        is UiState.Loading -> {
            val margin_16 = dimensionResource(R.dimen.margin_16)
            val margin_20 = dimensionResource(R.dimen.margin_20)
            Column(modifier = Modifier.padding(horizontal = margin_16)) {
                Image(
                    modifier = Modifier
                        .clickable { goBack() }
                        .padding(horizontal = margin_20, vertical = margin_16),
                    painter = painterResource(R.drawable.i_back),
                    contentDescription = null
                )
                when (viewModel.authPageMode) {
                    0 -> LoginScreen()
                    1 -> RegisterScreen()
                    2 -> ResetPasswordScreen()
                }
            }
        }
        else -> goBack()
    }
}

@Composable
fun LoginScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val size_2 = dimensionResource(R.dimen.margin_2)
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    val roundedShape = RoundedCornerShape(size_2)

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = margin_16),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Row(modifier = Modifier
            .border(size_2, shape = roundedShape, color = LoginBtnColor.copy(alpha = .25f))
            .fillMaxWidth()
        ) {
            TextButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LoginBtnColor.copy(alpha = .25f),
                    contentColor = Color.Black
                ),
                shape = roundedShape,
                contentPadding = PaddingValues(vertical = margin_16)
            ) { Text(stringResource(R.string.i_have_an_account)) }

            TextButton(
                onClick = { viewModel.authPageMode = 1 },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black.copy(alpha = 0.45f)
                ),
                shape = roundedShape,
                contentPadding = PaddingValues(vertical = margin_16)
            ) { Text(stringResource(R.string.i_don_t_have_an_account)) }

        }

        Column {
            TextField(value = viewModel.email,
                onValueChange = viewModel::setNewEmail,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = roundedShape,
                label = { Text(stringResource(R.string.email_placeholder)) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    focusedLabelColor = Color.Black
                )
            )
            TextField(value = viewModel.password,
                onValueChange = viewModel::setNewPassword,
                visualTransformation =
                    if(viewModel.isPasswordHidden) PasswordVisualTransformation()
                    else VisualTransformation.None,
                shape = roundedShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = margin_20),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    focusedLabelColor = Color.Black
                ),
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.i_show_password),
                        contentDescription = null,
                        modifier = Modifier
                            .size(margin_20)
                            .clickable {
                                viewModel.togglePasswordHidden()
                            }
                    )
                },
                singleLine = true,
                label = { Text(stringResource(R.string.password_placeholder)) },
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextButton(
                onClick = viewModel::signIn,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LoginBtnColor,
                    contentColor = Color.White
                ),
                shape = roundedShape,
                contentPadding = PaddingValues(vertical = margin_16)
            ) { Text(stringResource(R.string.login)) }

            Text(
                modifier = Modifier
                    .clickable{ viewModel.authPageMode = 2 },
                text = stringResource(R.string.forget_your_password),
                color = LoginBtnColor
            )
        }

    }

}
