package com.uriolus.recipes.feature.links_list.data.repository

import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkDao
import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkEntity
import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink
import com.uriolus.recipes.feature.links_list.domain.repository.RecipeLinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeLinkRepositoryImpl @Inject constructor(
    private val dao: RecipeLinkDao
) : RecipeLinkRepository {
    
    override fun getAllLinks(): Flow<List<RecipeLink>> {
        return dao.getAllLinks().map { entities ->
            entities.map { it.toRecipeLink() }
        }
    }
    
    override suspend fun insertLink(link: RecipeLink): Long {
        return dao.insertLink(RecipeLinkEntity.fromRecipeLink(link))
    }
    
    override suspend fun deleteLink(link: RecipeLink) {
        dao.deleteLink(RecipeLinkEntity.fromRecipeLink(link))
    }
    
    override suspend fun deleteLinkById(linkId: Long) {
        dao.deleteLinkById(linkId)
    }
}
