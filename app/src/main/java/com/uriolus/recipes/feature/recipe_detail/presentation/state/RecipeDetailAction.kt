package com.uriolus.recipes.feature.recipe_detail.presentation.state

sealed class RecipeDetailAction {
    data class LoadRecipe(val id: String) : RecipeDetailAction()
    object NavigateBack : RecipeDetailAction()
}
