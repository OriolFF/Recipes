package com.uriolus.recipes.feature.links_list.domain.use_case

import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink
import com.uriolus.recipes.feature.links_list.domain.repository.RecipeLinkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllLinksUseCase @Inject constructor(
    private val repository: RecipeLinkRepository
) {
    operator fun invoke(): Flow<List<RecipeLink>> {
        return repository.getAllLinks()
    }
}
