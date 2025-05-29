package com.uriolus.recipes.feature.links_list.domain.repository

import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink
import kotlinx.coroutines.flow.Flow

interface RecipeLinkRepository {
    fun getAllLinks(): Flow<List<RecipeLink>>
    suspend fun insertLink(link: RecipeLink): Long
    suspend fun deleteLink(link: RecipeLink)
    suspend fun deleteLinkById(linkId: Long)
}
