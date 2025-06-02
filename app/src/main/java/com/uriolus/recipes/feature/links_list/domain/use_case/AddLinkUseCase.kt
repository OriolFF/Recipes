package com.uriolus.recipes.feature.links_list.domain.use_case

import arrow.core.Either
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink
import com.uriolus.recipes.feature.links_list.domain.repository.RecipeLinkRepository
import javax.inject.Inject

class AddLinkUseCase @Inject constructor(
    private val repository: RecipeLinkRepository
) {
    suspend operator fun invoke(recipeLink: RecipeLink): Either<AppError, Long> {
        return repository.insertLink(recipeLink)
    }
    
    suspend operator fun invoke(url: String, title: String, description: String = "", thumbnailUrl: String = ""): Either<AppError, Long> {
        val link = RecipeLink(
            url = url,
            title = title,
            description = description,
            thumbnailUrl = thumbnailUrl
        )
        return repository.insertLink(link)
    }
}
