package com.uriolus.recipes.feature.extract_recipe.domain.use_case

import arrow.core.Either
import arrow.core.left
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject

class ExtractRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(url: String): Either<AppError, Recipe> {
        if (url.isBlank()) {
            return AppError.ValidationError("URL cannot be blank").left()
        }
        // Basic URL validation
        if (!android.util.Patterns.WEB_URL.matcher(url).matches()) {
            return AppError.ValidationError("Invalid URL format").left()
        }
        return repository.extractRecipeFromUrl(url)
    }
}
