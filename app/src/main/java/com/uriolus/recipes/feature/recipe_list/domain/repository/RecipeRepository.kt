package com.uriolus.recipes.feature.recipe_list.domain.repository

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getRecipes(): Flow<List<Recipe>>
    
    suspend fun extractRecipeFromUrl(url: String): Recipe?
}
