package com.uriolus.recipes.feature.recipe_list.data.source

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeDataSource {
    suspend fun getRecipes(): List<Recipe>
    suspend fun saveRecipes(recipes: List<Recipe>)
}
