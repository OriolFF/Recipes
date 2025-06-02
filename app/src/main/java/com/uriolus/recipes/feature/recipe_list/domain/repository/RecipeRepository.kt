package com.uriolus.recipes.feature.recipe_list.domain.repository

import arrow.core.Either
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe

interface RecipeRepository {
    suspend fun getRecipes(): Either<AppError, List<Recipe>> // Streams recipes, typically from local cache first

    /**
     * Fetches recipes from the remote source and updates the local cache.
     * Returns Either.Right(Unit) on success, Either.Left(AppError) on error.
     */
    suspend fun fetchAndCacheRecipes(): Either<AppError, Unit>

    /**
     * Extracts a recipe from a given URL.
     * The extracted recipe might be automatically cached.
     * Returns Either.Right(Recipe) if successful, Either.Left(AppError) otherwise.
     */
    suspend fun extractRecipeFromUrl(url: String): Either<AppError, Recipe>

    /**
     * Saves a single recipe to the local cache.
     * Returns Either.Right(Unit) on success, Either.Left(AppError) on error.
     */
    suspend fun saveRecipe(recipe: Recipe): Either<AppError, Unit>
}
