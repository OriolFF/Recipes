package com.uriolus.recipes.feature.recipe_list.data.repository

import android.util.Log
import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject
import javax.inject.Named
import kotlin.Result

class RecipeRepositoryImpl @Inject constructor(
    @Named("local") private val localDataSource: RecipeDataSource,
    @Named("remote") private val remoteDataSource: RecipeDataSource
) : RecipeRepository {

    override suspend fun getRecipes(): List<Recipe> {
        Log.d("RecipeRepositoryImpl", "Providing recipes List from local data source.")
        return localDataSource.getRecipes()
    }

    override suspend fun fetchAndCacheRecipes(): Result<Unit> {
        Log.d("RecipeRepositoryImpl", "Attempting to fetch and cache recipes from remote.")
        return try {
            val freshRecipes = remoteDataSource.getRecipes() 
            if (freshRecipes.isNotEmpty()) {
                localDataSource.saveRecipes(freshRecipes)
                Log.d("RecipeRepositoryImpl", "Fetched ${freshRecipes.size} recipes from remote and saved to local.")
                Result.success(Unit)
            } else {
                Log.w("RecipeRepositoryImpl", "Remote fetch returned empty list. No new data cached.")
                Result.success(Unit) 
            }
        } catch (e: Exception) {
            Log.e("RecipeRepositoryImpl", "Failed to fetch recipes from remote: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun extractRecipeFromUrl(url: String): Result<Recipe> {
        Log.d("RecipeRepositoryImpl", "Attempting to extract recipe from URL: $url")
        
        return try {
            val extractedRecipe = remoteDataSource.extractRecipeFromUrl(url) 
            if (extractedRecipe != null) {
                Log.d("RecipeRepositoryImpl", "Successfully extracted recipe: ${extractedRecipe.name}. Caching locally.")
                localDataSource.saveRecipe(extractedRecipe) 
                Result.success(extractedRecipe)
            } else {
                Log.w("RecipeRepositoryImpl", "Failed to extract recipe from URL or URL yielded no recipe.")
                Result.failure(Exception("No recipe found at the provided URL or extraction failed."))
            }
        } catch (e: Exception) {
            Log.e("RecipeRepositoryImpl", "Error extracting recipe from URL: ${e.message}", e)
            Result.failure(e)
        }
    }
}