package com.uriolus.recipes.feature.recipe_list.data.repository

import android.util.Log
import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject
import javax.inject.Named

class RecipeRepositoryImpl @Inject constructor(
    @Named("local") private val localDataSource: RecipeDataSource,
    @Named("remote") private val remoteDataSource: RecipeDataSource
) : RecipeRepository {

    override suspend fun getRecipes(): Either<AppError, List<Recipe>> =
        localDataSource.getRecipes()
            .mapLeft { error ->
                Log.e("RecipeRepository", "Failed to get local recipes: ${error.message}")
                error
            }

    override suspend fun fetchAndCacheRecipes(): Either<AppError, Unit> {
        return remoteDataSource.getRecipes()
            .flatMap { remoteRecipes ->
            if (remoteRecipes.isNotEmpty()) {
                localDataSource.saveRecipes(remoteRecipes).map {
                    Log.d("RecipeRepository", "Successfully cached ${remoteRecipes.size} recipes")
                    // Unit is implicitly returned by map if successful
                }.mapLeft { error ->
                    Log.e("RecipeRepository", "Failed to cache recipes: ${error.message}")
                    error // Propagate the error from localDataSource
                }
            } else {
                Log.w("RecipeRepository", "Remote fetch returned empty recipe list, nothing to cache.")
                Unit.right() // Success, even if no recipes to cache
            }
        }.mapLeft { error ->
            Log.e("RecipeRepository", "Failed to fetch recipes from remote: ${error.message}")
            error // Propagate the error from remoteDataSource
        }
    }

    override suspend fun extractRecipeFromUrl(url: String): Either<AppError, Recipe> {
        Log.d("RecipeRepository", "Extracting recipe from URL: $url")
        
        return when (val result = remoteDataSource.extractRecipeFromUrl(url)) {
            is Either.Right -> {
                val recipe = result.value
                if (recipe != null) {
                    // Save the recipe to local cache, but don't fail if it doesn't work
                    localDataSource.saveRecipe(recipe)
                        .onLeft { error ->
                            Log.w("RecipeRepository", "Failed to cache extracted recipe: ${error.message}")
                        }
                    // Return the recipe even if caching failed
                    recipe.right()
                } else {
                    AppError.NotFoundError("No recipe found at the provided URL").left()
                }
            }
            is Either.Left -> {
                Log.e("RecipeRepository", "Failed to extract recipe: ${result.value.message}")
                result
            }
        }
    }

    override suspend fun saveRecipe(recipe: Recipe): Either<AppError, Unit> {
        Log.d("RecipeRepository", "Saving recipe: ${recipe.name}")
        return localDataSource.saveRecipe(recipe).mapLeft { error ->
            Log.e("RecipeRepository", "Failed to save recipe locally: ${error.message}")
            error
        }
    }
}