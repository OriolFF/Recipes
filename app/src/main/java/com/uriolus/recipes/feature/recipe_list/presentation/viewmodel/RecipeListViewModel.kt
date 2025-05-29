package com.uriolus.recipes.feature.recipe_list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.recipes.core.data.remote.AuthenticationException
import com.uriolus.recipes.feature.auth.domain.use_case.LogoutUseCase
import com.uriolus.recipes.feature.recipe_list.domain.use_case.GetRecipesUseCase
import com.uriolus.recipes.feature.recipe_list.presentation.state.RecipeListAction
import com.uriolus.recipes.feature.recipe_list.presentation.state.RecipeListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeListState())
    val state: StateFlow<RecipeListState> = _state.asStateFlow()

    init {
        handleAction(RecipeListAction.LoadRecipes)
    }

    fun handleAction(action: RecipeListAction) {
        when (action) {
            is RecipeListAction.LoadRecipes -> loadRecipes()
            is RecipeListAction.RecipeClicked -> {
                // Handle navigation to recipe detail
            }
            is RecipeListAction.LogoutClicked -> {
                viewModelScope.launch {
                    logoutUseCase()
                    _state.value = _state.value.copy(logoutRequested = true)
                }
            }
        }
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val recipes = getRecipesUseCase.exec()
                _state.value = _state.value.copy(
                    recipes = recipes,
                    isLoading = false,
                    authenticationErrorOccurred = false // Reset on successful load
                )
            } catch (e: Exception) {
                if (e is AuthenticationException) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Authentication error",
                        authenticationErrorOccurred = true
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun onLogoutHandled() {
        _state.value = _state.value.copy(logoutRequested = false)
    }

    fun onAuthenticationErrorHandled() {
        _state.value = _state.value.copy(authenticationErrorOccurred = false, error = null)
    }
}
