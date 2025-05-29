package com.uriolus.recipes.feature.recipe_list.domain.use_case

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend fun exec(): List<Recipe> {
        repository.fetchAndCacheRecipes() // Attempt to refresh cache
        return repository.getRecipes()    // Get from cache
    }
}
