package pl.sofantastica.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    afterSignIn: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val isSignedIn by viewModel.isSignedIn.collectAsState()
    val errorText by viewModel.errorText.collectAsState()

    if (isSignedIn) {
        afterSignIn()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).onFocusChanged { viewModel.clearErrorText() }, Arrangement.Center) {
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
        Text(text = errorText,
            modifier = Modifier.height(56.dp),
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp
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
