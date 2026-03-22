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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.furniture.duet.R
import com.furniture.duet.ui.common.UiState
import com.furniture.duet.ui.theme.LoginBtnColor

@Composable
fun RegisterScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val tipColor = Color.Black.copy(.45f)

    val size_2 = dimensionResource(R.dimen.margin_2)
    val margin_5 = dimensionResource(R.dimen.margin_5)
    val margin_16 = dimensionResource(R.dimen.margin_16)
    val margin_20 = dimensionResource(R.dimen.margin_20)

    val roundedShape = RoundedCornerShape(size_2)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = margin_16)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Row(
            modifier = Modifier
                .border(size_2, shape = roundedShape, color = LoginBtnColor.copy(alpha = .25f))
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = { viewModel.authPageMode = 0 },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black.copy(alpha = 0.45f)
                ),
                shape = roundedShape,
                contentPadding = PaddingValues(vertical = margin_16)
            ) { Text(stringResource(R.string.i_have_an_account)) }

            TextButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LoginBtnColor.copy(alpha = .25f),
                    contentColor = Color.Black
                ),
                shape = roundedShape,
                contentPadding = PaddingValues(vertical = margin_16)
            ) { Text(stringResource(R.string.i_don_t_have_an_account)) }

        }

        TextField(
            value = viewModel.fullName,
            onValueChange = viewModel::setNewFullName,
            modifier = Modifier.fillMaxWidth().padding(top = margin_20),
            singleLine = true,
            shape = roundedShape,
            label = { Text(stringResource(R.string.full_name_placeholder)) },
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
        TextField(
            value = viewModel.phoneNumber,
            onValueChange = viewModel::setNewPhoneNumber,
            modifier = Modifier.fillMaxWidth().padding(top = margin_20),
            singleLine = true,
            shape = roundedShape,
            label = { Text(stringResource(R.string.phone_placeholder)) },
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
        TextField(
            value = viewModel.email,
            onValueChange = viewModel::setNewEmail,
            modifier = Modifier.fillMaxWidth().padding(top = margin_20),
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
        TextField(
            value = viewModel.password,
            onValueChange = viewModel::setNewPassword,
            visualTransformation =
                if (viewModel.isPasswordHidden) PasswordVisualTransformation()
                else VisualTransformation.None,
            shape = roundedShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = margin_20),
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

        Text(
            text = "•\t\tat least 8 characters",
            color = tipColor,
            modifier = Modifier.fillMaxWidth().padding(start = margin_5)
        )
        Text(
            text = "•\t\tat least one uppercase letter",
            color = tipColor,
            modifier = Modifier.fillMaxWidth().padding(start = margin_5)
        )
        Text(
            text = "•\t\tat least one number",
            color = tipColor,
            modifier = Modifier.fillMaxWidth().padding(start = margin_5)
        )
        Text(
            text = "•\t\tat least one special characters",
            color = tipColor,
            modifier = Modifier.fillMaxWidth().padding(start = margin_5)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier
                    .padding(horizontal = size_2, vertical = margin_16)
                    .size(margin_16),
                checked = viewModel.isPrivacyPolicyChecked,
                onCheckedChange = { viewModel.isPrivacyPolicyChecked = it },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = tipColor,
                    uncheckedColor = tipColor,
                    checkedColor = tipColor,
                )
            )
            Text(
                text = "\tI agree to Privacy Policy",
                color = tipColor,
                modifier = Modifier.fillMaxWidth()
            )
        }

        TextButton(
            onClick = viewModel::signUp,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = LoginBtnColor,
                contentColor = Color.White
            ),
            shape = roundedShape,
            contentPadding = PaddingValues(vertical = margin_16)
        ) { Text(stringResource(R.string.register)) }
    }

}
