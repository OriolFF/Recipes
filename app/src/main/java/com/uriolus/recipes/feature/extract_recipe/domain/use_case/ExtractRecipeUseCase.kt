package com.uriolus.recipes.feature.extract_recipe.domain.use_case

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject

class ExtractRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(url: String): Result<Recipe> {
        if (url.isBlank()) {
            return Result.failure(IllegalArgumentException("URL cannot be blank."))
        }
        // Basic URL validation
        if (!android.util.Patterns.WEB_URL.matcher(url).matches()) {
            return Result.failure(IllegalArgumentException("Invalid URL format."))
        }
        return repository.extractRecipeFromUrl(url)
    }
}
