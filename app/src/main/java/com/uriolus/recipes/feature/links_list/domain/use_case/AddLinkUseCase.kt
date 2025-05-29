package com.uriolus.recipes.feature.links_list.domain.use_case

import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink
import com.uriolus.recipes.feature.links_list.domain.repository.RecipeLinkRepository
import javax.inject.Inject

class AddLinkUseCase @Inject constructor(
    private val repository: RecipeLinkRepository
) {
    suspend operator fun invoke(recipeLink: RecipeLink): Long {
        return repository.insertLink(recipeLink)
    }
    
    suspend operator fun invoke(url: String, title: String, description: String = "", thumbnailUrl: String = ""): Long {
        val link = RecipeLink(
            url = url,
            title = title,
            description = description,
            thumbnailUrl = thumbnailUrl
        )
        return repository.insertLink(link)
    }
}
