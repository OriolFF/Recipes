package com.uriolus.recipes.feature.recipe_list.data.source.remote

import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of RecipeDataSource that fetches data from the remote API
 */
class RemoteRecipeDataSource @Inject constructor(
    private val apiClient: RecipeApiClient
) : RecipeDataSource {
    
    override fun getRecipes(): Flow<List<Recipe>> = flow {
        try {
            val recipes = apiClient.getAllRecipes().map { it.toDomainModel() }
            emit(recipes)
        } catch (e: Exception) {
            // In a real app, you'd want to handle errors more gracefully
            // and potentially emit an empty list or cached data
            emit(emptyList())
        }
    }
    
    /**
     * Extract recipe data from a URL
     */
    suspend fun extractRecipeFromUrl(url: String): Recipe? {
        return try {
            apiClient.extractRecipeFromUrl(url).toDomainModel()
        } catch (e: Exception) {
            null
        }
    }
}
