package com.uriolus.recipes.feature.recipe_list.domain.use_case

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject

class ExtractRecipeFromUrlUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(url: String): Result<Recipe> {
        return try {
            val recipe = repository.extractRecipeFromUrl(url)
            if (recipe != null) {
                Result.success(recipe)
            } else {
                Result.failure(Exception("Failed to extract recipe from URL"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
