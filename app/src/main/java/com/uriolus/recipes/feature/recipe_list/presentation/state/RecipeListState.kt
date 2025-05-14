package com.uriolus.recipes.feature.recipe_list.presentation.state

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe

data class RecipeListState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
