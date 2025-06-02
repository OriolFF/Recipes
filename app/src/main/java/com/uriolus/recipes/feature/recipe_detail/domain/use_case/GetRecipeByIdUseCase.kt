package com.uriolus.recipes.feature.recipe_detail.domain.use_case

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipeByIdUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend fun exec(id: String): Either<AppError, Recipe> {
        return repository.getRecipes().flatMap { recipes ->
            val recipe = recipes.find { it.id == id }
            recipe?.right() ?: AppError.NotFoundError("Recipe with id $id not found").left()
        }
    }
}

