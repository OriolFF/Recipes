package com.uriolus.recipes.feature.recipe_list.domain.use_case

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject

class ExtractRecipeFromUrlUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(url: String): Result<Recipe> {
        return try {
            repository.extractRecipeFromUrl(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
