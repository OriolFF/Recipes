package com.uriolus.recipes.feature.recipe_detail.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.recipes.feature.recipe_detail.domain.use_case.GetRecipeByIdUseCase
import com.uriolus.recipes.feature.recipe_detail.presentation.state.RecipeDetailAction
import com.uriolus.recipes.feature.recipe_detail.presentation.state.RecipeDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeDetailState())
    val state: StateFlow<RecipeDetailState> = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("recipeId")?.let { recipeId ->
            handleAction(RecipeDetailAction.LoadRecipe(recipeId))
        }
    }

    fun handleAction(action: RecipeDetailAction) {
        when (action) {
            is RecipeDetailAction.LoadRecipe -> loadRecipe(action.id)
            is RecipeDetailAction.NavigateBack -> { /* Handle in the UI layer */
            }
        }
    }

    private fun loadRecipe(id: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getRecipeByIdUseCase.exec(id)
                .fold({ error ->
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }, { recipe ->
                    _state.update { it.copy(recipe = recipe, isLoading = false) }
                })


        }

    }

}
