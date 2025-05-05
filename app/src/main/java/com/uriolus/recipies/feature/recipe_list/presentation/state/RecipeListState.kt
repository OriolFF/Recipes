package com.uriolus.recipies.feature.recipe_list.presentation.state

import com.uriolus.recipies.feature.recipe_list.domain.model.Recipe

data class RecipeListState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
