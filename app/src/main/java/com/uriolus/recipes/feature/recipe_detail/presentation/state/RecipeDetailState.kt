package com.uriolus.recipes.feature.recipe_detail.presentation.state

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe

data class RecipeDetailState(
    val recipe: Recipe? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
