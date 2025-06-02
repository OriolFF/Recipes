package com.uriolus.recipes.feature.recipe_list.domain.use_case

import arrow.core.Either
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject

class SaveRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe): Either<AppError, Unit> {
        // Input validation for the recipe can be added here if needed
        // For example, check if recipe.title is not blank, etc.
        // If validation fails, return an AppError.ValidationError.left()

        return recipeRepository.saveRecipe(recipe)
    }
}
