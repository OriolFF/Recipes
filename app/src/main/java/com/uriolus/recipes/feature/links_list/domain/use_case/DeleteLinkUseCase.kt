package com.uriolus.recipes.feature.links_list.domain.use_case

import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink
import com.uriolus.recipes.feature.links_list.domain.repository.RecipeLinkRepository
import javax.inject.Inject

class DeleteLinkUseCase @Inject constructor(
    private val repository: RecipeLinkRepository
) {
    suspend operator fun invoke(link: RecipeLink) {
        repository.deleteLink(link)
    }
}
