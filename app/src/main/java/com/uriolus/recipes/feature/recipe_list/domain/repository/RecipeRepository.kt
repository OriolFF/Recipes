package com.uriolus.recipes.feature.recipe_list.domain.repository

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe

interface RecipeRepository {
    suspend fun getRecipes(): List<Recipe> // Streams recipes, typically from local cache first

    /**
     * Fetches recipes from the remote source and updates the local cache.
     * Returns Result.success(Unit) on success, Result.failure(Exception) on error.
     */
    suspend fun fetchAndCacheRecipes(): Result<Unit>

    /**
     * Extracts a recipe from a given URL.
     * The extracted recipe might be automatically cached.
     * Returns Result.success(Recipe) if successful, Result.failure(Exception) otherwise.
     */
    suspend fun extractRecipeFromUrl(url: String): Result<Recipe>
}
