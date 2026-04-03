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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.furniture.duet.R
import com.furniture.duet.ui.main.ErrorUI
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.theme.EnabledBtnColor

@Composable
fun LoginRoute(
    goBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    when(state) {
        is UiState.Error -> ErrorUI("Error: ${state.throwable.message}")
        is UiState.Loading -> {
            if (viewModel.isLogin) {
                LoginScreen()
            } else {
                RegisterScreen()
            }
        }
        else -> goBack()
    }
}

@Composable
fun LoginScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    val roundedShape = RoundedCornerShape(margin_20)

    Column(
        modifier = Modifier.fillMaxSize().padding(margin_16),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Row(modifier = Modifier
            .background(Color.White, roundedShape)
            .fillMaxWidth()
        ) {
            TextButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnabledBtnColor,
                    contentColor = Color.White
                ),
                shape = roundedShape,
                contentPadding = PaddingValues(vertical = margin_16)
            ) { Text(stringResource(R.string.i_have_an_account)) }

            TextButton(
                onClick = { viewModel.isLogin = false },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = EnabledBtnColor
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
                label = {
                    Text(
                        text = stringResource(R.string.email_placeholder),
                        color = EnabledBtnColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = EnabledBtnColor,
                    focusedTextColor = EnabledBtnColor,
                    unfocusedLabelColor = EnabledBtnColor,
                    focusedLabelColor = EnabledBtnColor
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
                    unfocusedTextColor = EnabledBtnColor,
                    focusedTextColor = EnabledBtnColor,
                    unfocusedLabelColor = EnabledBtnColor,
                    focusedLabelColor = EnabledBtnColor
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
                label = {
                    Text(
                        text = stringResource(R.string.password_placeholder),
                        color = EnabledBtnColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextButton(
                onClick = viewModel::signIn,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnabledBtnColor,
                    contentColor = Color.White
                ),
                shape = roundedShape,
                contentPadding = PaddingValues(vertical = margin_16)
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Text(
                modifier = Modifier
                    .clickable{ viewModel.isForgotPassword = true },
                text = stringResource(R.string.forget_your_password),
                color = EnabledBtnColor
            )

            if (viewModel.isForgotPassword) {
                ResetPasswordScreen()
            }
        }

    }

}
