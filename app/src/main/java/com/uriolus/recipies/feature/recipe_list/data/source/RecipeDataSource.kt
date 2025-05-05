package com.uriolus.recipies.feature.recipe_list.data.source

import com.uriolus.recipies.feature.recipe_list.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeDataSource {
    fun getRecipes(): Flow<List<Recipe>>
}
