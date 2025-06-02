package com.uriolus.recipes.feature.links_list.data.repository

import arrow.core.Either
import arrow.core.catch
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkDao
import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkEntity
import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink
import com.uriolus.recipes.feature.links_list.domain.repository.RecipeLinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeLinkRepositoryImpl @Inject constructor(
    private val dao: RecipeLinkDao
) : RecipeLinkRepository {
    
    override fun getAllLinks(): Flow<List<RecipeLink>> {
        return dao.getAllLinks()
            .map { entities ->
                try {
                    entities.map { it.toRecipeLink() }
                } catch (e: Exception) {
                    // Log the error and return empty list
                    e.printStackTrace()
                    emptyList()
                }
            }
            .catch { emit(emptyList()) } // Return empty list on error
    }
    
    override suspend fun insertLink(link: RecipeLink): Either<AppError, Long> =
        try {
            dao.insertLink(RecipeLinkEntity.fromRecipeLink(link)).right()
        } catch (e: Exception) {
            AppError.DatabaseError("Failed to insert link", e).left()
        }
    
    override suspend fun deleteLink(link: RecipeLink): Either<AppError, Unit> =
        try {
            dao.deleteLink(RecipeLinkEntity.fromRecipeLink(link))
            Unit.right()
        } catch (e: Exception) {
            AppError.DatabaseError("Failed to delete link", e).left()
        }
    
    override suspend fun deleteLinkById(linkId: Long): Either<AppError, Unit> =
        try {
            dao.deleteLinkById(linkId)
            Unit.right()
        } catch (e: Exception) {
            AppError.DatabaseError("Failed to delete link by id: $linkId", e).left()
        }
}
