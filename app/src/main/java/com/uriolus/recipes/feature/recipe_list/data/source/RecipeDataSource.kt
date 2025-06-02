package com.uriolus.recipes.feature.recipe_list.data.source

import arrow.core.Either
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeDataSource {
    suspend fun getRecipes(): Either<AppError, List<Recipe>>
    suspend fun saveRecipes(recipes: List<Recipe>): Either<AppError, Unit>
    suspend fun saveRecipe(recipe: Recipe): Either<AppError, Unit>
    suspend fun extractRecipeFromUrl(url: String): Either<AppError, Recipe?>
}
