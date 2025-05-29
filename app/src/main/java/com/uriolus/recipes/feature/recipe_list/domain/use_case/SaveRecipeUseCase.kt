package com.uriolus.recipes.feature.recipe_list.domain.use_case

import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkEntity
import com.uriolus.recipes.feature.links_list.domain.repository.RecipeLinkRepository
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import javax.inject.Inject

class SaveRecipeUseCase @Inject constructor(
    private val recipeLinkRepository: RecipeLinkRepository
) {
    suspend operator fun invoke(recipe: Recipe): Result<Unit> {
        return try {
            val entity = RecipeLinkEntity(
                id = recipe.id.toLongOrNull() ?: 0L,
                title = recipe.name,
                description = recipe.description,
                thumbnailUrl = recipe.imageUrl?:"",
                url = recipe.imageUrl?:"",
                createdAt = System.currentTimeMillis()
            )
            recipeLinkRepository.insertLink(entity.toRecipeLink())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
