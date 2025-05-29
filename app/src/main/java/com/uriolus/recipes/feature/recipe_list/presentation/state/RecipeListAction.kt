package com.uriolus.recipes.feature.recipe_list.presentation.state

sealed class RecipeListAction {
    data object LoadRecipes : RecipeListAction()
    data class RecipeClicked(val recipeId: String) : RecipeListAction()
    data object LogoutClicked : RecipeListAction()
}
