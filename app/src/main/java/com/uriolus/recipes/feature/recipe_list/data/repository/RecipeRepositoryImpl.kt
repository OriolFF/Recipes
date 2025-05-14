package com.uriolus.recipes.feature.recipe_list.data.repository

import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val dataSource: RecipeDataSource
) : RecipeRepository {
    
    override fun getRecipes(): Flow<List<Recipe>> {
        return dataSource.getRecipes()
    }
}
