package com.uriolus.recipes.feature.recipe_list.data.source.local

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.data.source.local.dao.RecipeDao
import com.uriolus.recipes.feature.recipe_list.data.source.local.model.toDomain
import com.uriolus.recipes.feature.recipe_list.data.source.local.model.toEntity
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import javax.inject.Inject

class LocalRecipeDataSource @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeDataSource {

    override suspend fun getRecipes(): Either<AppError, List<Recipe>> =
        try {
            // Assuming recipeDao.getAllRecipes() returns List<RecipeEntity>
            // and toDomain is an extension on RecipeEntity
            val recipes = recipeDao.getAllRecipes().map { it.toDomain() }
            recipes.right()
        } catch (e: Exception) {
            AppError.LocalDatabaseError("Failed to fetch recipes from local source: ${e.message}", e).left()
        }

    override suspend fun saveRecipes(recipes: List<Recipe>): Either<AppError, Unit> =
        try {
            // Assuming toEntity is an extension on Recipe, mapping a list
            val entities = recipes.map { it.toEntity() }
            recipeDao.insertAll(entities)
            Unit.right()
        } catch (e: Exception) {
            AppError.LocalDatabaseError("Failed to save recipes to local source: ${e.message}", e).left()
        }

    override suspend fun saveRecipe(recipe: Recipe): Either<AppError, Unit> =
        try {
            recipeDao.insertRecipe(recipe.toEntity())
            Unit.right()
        } catch (e: Exception) {
            AppError.LocalDatabaseError("Failed to save recipe to local source: ${e.message}", e).left()
        }

    override suspend fun extractRecipeFromUrl(url: String): Either<AppError, Recipe?> =
        try {
            val recipe = recipeDao.getRecipeByUrl(url)?.toDomain()
            recipe.right() // recipe can be null, which is valid for Either<AppError, Recipe?>
        } catch (e: Exception) {
            AppError.LocalDatabaseError("Failed to find recipe by URL from local source: ${e.message}", e).left()
        }
}
