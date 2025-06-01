package com.uriolus.recipes.feature.recipe_list.data.source.remote

import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import javax.inject.Inject

/**
 * Implementation of RecipeDataSource that fetches data from the remote API
 */
class RemoteRecipeDataSource @Inject constructor(
    private val apiClient: RecipeApiClient
) : RecipeDataSource {
    
    override suspend fun getRecipes(): List<Recipe> {
        return try {
            apiClient.getAllRecipes().map { it.toDomain() }
        } catch (e: Exception) {
            // Log error or handle as needed
            emptyList()
        }
    }

    // Added to fulfill RecipeDataSource interface
    override suspend fun saveRecipes(recipes: List<Recipe>) {
        // This operation might not be directly supported or needed for a remote source,
        // or it might involve complex logic (e.g., individual POST/PUT requests per recipe).
        // For now, it's a no-op. The primary responsibility for saving/caching
        // will likely fall to the LocalRecipeDataSource via the repository.
        // If your API supports bulk saving, implement it here.
        // Example: recipes.forEach { apiClient.saveRecipe(it.toDto()) }
    }

    override suspend fun saveRecipe(recipe: Recipe) {
        // Remote source typically doesn't save individual recipes this way
        throw UnsupportedOperationException("Saving a single recipe is not supported by RemoteRecipeDataSource")
    }
    
    /**
     * Extract recipe data from a URL
     */
    override suspend fun extractRecipeFromUrl(url: String): Recipe? {
        return try {
            apiClient.extractRecipeFromUrl(url).toDomain()
        } catch (e: Exception) {
            // Log error or handle as needed
            null
        }
    }
}
