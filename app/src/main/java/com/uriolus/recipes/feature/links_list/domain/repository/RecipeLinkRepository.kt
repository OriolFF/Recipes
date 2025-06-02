package com.uriolus.recipes.feature.links_list.domain.repository

import arrow.core.Either
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink
import kotlinx.coroutines.flow.Flow

interface RecipeLinkRepository {
    fun getAllLinks(): Flow<List<RecipeLink>>
    suspend fun insertLink(link: RecipeLink): Either<AppError, Long>
    suspend fun deleteLink(link: RecipeLink): Either<AppError, Unit>
    suspend fun deleteLinkById(linkId: Long): Either<AppError, Unit>
}
