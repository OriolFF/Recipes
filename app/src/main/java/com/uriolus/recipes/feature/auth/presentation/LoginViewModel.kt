package com.uriolus.recipes.feature.auth.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.uriolus.recipes.core.common.Resource
import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import com.uriolus.recipes.core.data.preferences.TokenStorageManager
import com.uriolus.recipes.feature.auth.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null,
    val tokenResponse: TokenResponse? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenStorageManager: TokenStorageManager
) : ViewModel() {

    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            when (val result = loginUseCase(_username.value, _password.value)) {
                is Resource.Success -> {
                    result.data?.accessToken?.let {
                        tokenStorageManager.saveAccessToken(it)
                    }
                    _loginState.value = LoginState(loginSuccess = true, tokenResponse = result.data)
                    // Navigation is handled by the LoginScreen's onLoginSuccess callback
                }
                is Resource.Error -> {
                    Log.e("LoginViewModel", "Login failed: ${result.message}")
                    _loginState.value = LoginState(error = result.message)
                }
                is Resource.Loading -> {
                    // This case might not be directly emitted by the current LoginUseCase setup
                    // but is good practice to handle if the use case changes.
                    _loginState.value = LoginState(isLoading = true)
                }
            }
        }
    }
}
