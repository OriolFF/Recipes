package com.uriolus.recipes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.recipes.core.data.preferences.TokenStorageManager
import com.uriolus.recipes.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class SplashUiState {
    object Loading : SplashUiState()
    data class InitialRoute(val route: String) : SplashUiState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    tokenStorageManager: TokenStorageManager
) : ViewModel() {

    val splashUiState: StateFlow<SplashUiState> =
        tokenStorageManager.accessTokenFlow
            .map { token ->
                if (token.isNullOrBlank()) {
                    SplashUiState.InitialRoute(NavRoutes.LOGIN)
                } else {
                    SplashUiState.InitialRoute(NavRoutes.RECIPE_LIST)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SplashUiState.Loading
            )
}
