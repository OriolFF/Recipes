package com.uriolus.recipies.feature.recipe_detail.presentation.state

import com.uriolus.recipies.feature.recipe_list.domain.model.Recipe

data class RecipeDetailState(
    val recipe: Recipe? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
