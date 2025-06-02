package com.uriolus.recipes.feature.recipe_list.domain.use_case

import arrow.core.Either
import arrow.core.flatMap
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend fun exec(): Either<AppError, List<Recipe>> {
        // Try to refresh the cache, but don't fail if it doesn't work
        when (val result = repository.fetchAndCacheRecipes()) {
            is Either.Left -> {
                // Log the error but continue with cached data if available
                println("Failed to refresh recipes cache: ${result.value.message}")
            }
            is Either.Right -> {
                // Cache refresh successful
                println("Successfully refreshed recipes cache")
            }
        }
        // Return the cached data (which might be stale if refresh failed)
        return repository.getRecipes()
    }
}
