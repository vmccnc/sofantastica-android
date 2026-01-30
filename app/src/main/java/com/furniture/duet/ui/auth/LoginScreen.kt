package com.furniture.duet.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.furniture.duet.ui.ErrorUI
import com.furniture.duet.ui.common.UiState

@Composable
fun LoginRoute(
    afterSignIn: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    if (state is UiState.Error) {
        ErrorUI("Error: ${state.throwable.message}")
    } else {
        LoginScreen(afterSignIn)
    }
}

@Composable
fun LoginScreen(
    afterSignIn: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val uiState = viewModel.uiState

    if (uiState is UiState.Success) {
        afterSignIn()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), Arrangement.Center) {
        TextField(value = email,
            onValueChange = viewModel::setEmail,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp),
            singleLine = true,
            label = { Text("Email") }
        )
        TextField(value = password,
            onValueChange = viewModel::setPassword,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp),
            singleLine = true,
            label = { Text("Password") },
        )
        Row(modifier = Modifier.fillMaxWidth(), Arrangement.Center) {
            TextButton(onClick = viewModel::signIn) {
                Text("Sign In")
            }
            TextButton(onClick = viewModel::signUp) {
                Text("Sign Up")
            }
        }
    }
}
