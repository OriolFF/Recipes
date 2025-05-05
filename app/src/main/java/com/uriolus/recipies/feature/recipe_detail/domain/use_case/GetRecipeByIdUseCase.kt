package com.uriolus.recipies.feature.recipe_detail.domain.use_case

import com.uriolus.recipies.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipies.feature.recipe_list.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecipeByIdUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(id: String): Flow<Recipe?> {
        return repository.getRecipes().map { recipes ->
            recipes.find { it.id == id }
        }
    }
}
