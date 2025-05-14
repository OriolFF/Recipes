package com.uriolus.recipes.feature.recipe_list.domain.use_case

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Recipe>> {
        return repository.getRecipes()
    }
}
