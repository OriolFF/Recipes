package com.uriolus.recipes.feature.auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uriolus.recipes.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit // Callback for successful login
) {
    val username by viewModel.username
    val password by viewModel.password
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current // For potential Toast messages

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(id = R.string.login_title), style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text(stringResource(id = R.string.login_username_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(stringResource(id = R.string.login_password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (loginState.isLoading) {
            CircularProgressIndicator()
        }

        loginState.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { viewModel.login() },
            enabled = !loginState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.login_button_text))
        }

        // Handle login success
        if (loginState.loginSuccess && loginState.tokenResponse != null) {
            // For now, we can just show a toast or log, navigation will be next
            // LaunchedEffect(loginState.tokenResponse) { // Ensure it runs once
            //    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
            // }
            onLoginSuccess()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // This preview won't have a real ViewModel, so interactions won't work.
    // You might need to create a fake ViewModel or pass initial state for better previews.
    MaterialTheme {
        LoginScreen(onLoginSuccess = {})
    }
}
