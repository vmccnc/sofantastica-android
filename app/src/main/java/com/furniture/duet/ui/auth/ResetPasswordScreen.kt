package com.furniture.duet.ui.auth

import androidx.compose.foundation.Image
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
import com.furniture.duet.ui.theme.LoginBtnColor

@Composable
fun ResetPasswordScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val size_2 = dimensionResource(R.dimen.margin_2)
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_20 = dimensionResource(R.dimen.margin_20)
    val roundedShape = RoundedCornerShape(size_2)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = margin_16),
        verticalArrangement = Arrangement.SpaceAround
    ) {

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

        TextButton(
            onClick = viewModel::resetPassword,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = LoginBtnColor,
                contentColor = Color.White
            ),
            shape = roundedShape,
            contentPadding = PaddingValues(vertical = margin_16)
        ) { Text(stringResource(R.string.reset_password)) }

    }
}
