package com.uriolus.recipes.feature.recipe_list.domain.use_case

import arrow.core.Either
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject

class ExtractRecipeFromUrlUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(url: String): Either<AppError, Recipe> =
        repository.extractRecipeFromUrl(url)
}
