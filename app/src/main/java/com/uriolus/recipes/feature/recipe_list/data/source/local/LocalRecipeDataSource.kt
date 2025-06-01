package com.uriolus.recipes.feature.recipe_list.data.source.local

import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.data.source.local.dao.RecipeDao
import com.uriolus.recipes.feature.recipe_list.data.source.local.model.toDomain
import com.uriolus.recipes.feature.recipe_list.data.source.local.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRecipeDataSource @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeDataSource {

    override suspend fun getRecipes(): List<Recipe> {
        return recipeDao.getAllRecipes().map { entities ->
            entities.toDomain()
        }
    }

    override suspend fun saveRecipes(recipes: List<Recipe>) {
        // Optional: Clear old cache before inserting new data for a full refresh
        // For now, we'll just replace, which is handled by OnConflictStrategy.REPLACE in DAO
        // recipeDao.deleteAll() 
        recipeDao.insertAll(recipes.toEntity())
    }
}
