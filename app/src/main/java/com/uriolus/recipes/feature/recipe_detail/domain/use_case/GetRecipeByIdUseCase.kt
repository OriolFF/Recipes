package com.uriolus.recipes.feature.recipe_detail.domain.use_case

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetRecipeByIdUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend fun exec(id: String): Recipe?{
        return repository.getRecipes().find { it.id == id }
        }
    }

