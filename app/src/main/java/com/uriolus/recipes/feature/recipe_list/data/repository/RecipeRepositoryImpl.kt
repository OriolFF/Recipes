package com.uriolus.recipes.feature.recipe_list.data.repository

import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.data.source.remote.RemoteRecipeDataSource
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class RecipeRepositoryImpl @Inject constructor(
    @Named("local") private val localDataSource: RecipeDataSource,
    @Named("remote") private val remoteDataSource: RecipeDataSource,
    private val remoteRecipeDataSource: RemoteRecipeDataSource
) : RecipeRepository {
    
    override fun getRecipes(): Flow<List<Recipe>> = flow {
        // First try to get recipes from the remote source
        try {
            emitAll(remoteDataSource.getRecipes())
        } catch (e: Exception) {
            // If remote fails, fall back to local data
            emitAll(localDataSource.getRecipes().catch { emit(emptyList()) })
        }
    }
    
   override suspend fun extractRecipeFromUrl(url: String): Recipe? {
        return remoteRecipeDataSource.extractRecipeFromUrl(url)
    }
}
