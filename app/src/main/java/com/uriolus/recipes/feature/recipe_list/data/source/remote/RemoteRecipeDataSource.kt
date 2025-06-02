package com.uriolus.recipes.feature.recipe_list.data.source.remote

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.core.data.remote.AuthenticationException
import com.uriolus.recipes.core.data.remote.ForbiddenException
import com.uriolus.recipes.core.data.remote.NotFoundException
import com.uriolus.recipes.core.data.remote.ValidationException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.ConnectException
import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import javax.inject.Inject

/**
 * Implementation of RecipeDataSource that fetches data from the remote API
 */
class RemoteRecipeDataSource @Inject constructor(
    private val apiClient: RecipeApiClient
) : RecipeDataSource {

    override suspend fun getRecipes(): Either<AppError, List<Recipe>> =
        try {
            apiClient.getAllRecipes()
                .flatMap { recipes: List<RecipeApi> ->
                    recipes.map { it.toDomain() }.right()
                }
        } catch (e: Exception) {
            when (e) {
                is AuthenticationException -> AppError.UnauthorizedError(
                    e.message ?: "Not authenticated", e
                )

                is ForbiddenException -> AppError.ForbiddenError(e.message ?: "Forbidden$e")
                is NotFoundException -> AppError.NotFoundError(e.message ?: "Not found$e")
                is ValidationException -> AppError.ValidationError(
                    e.message ?: "Validation error"
                )

                is SocketTimeoutException -> AppError.NetworkError(
                    "Request timed out. Please try again."+
                    e
                )

                is UnknownHostException, is ConnectException -> AppError.NetworkError(
                    "Cannot connect to server. Please check your internet connection.",
                    e
                )

                else -> AppError.NetworkError(
                    "Failed to fetch recipes from remote: ${e.message ?: "Unknown error"}",
                    e
                )
            }.left()
        }

    override suspend fun saveRecipes(recipes: List<Recipe>): Either<AppError, Unit> =
    // This operation might not be directly supported or needed for a remote source,
    // or it might involve complex logic (e.g., individual POST/PUT requests per recipe).
    // For now, it's a no-op. The primary responsibility for saving/caching
        // will likely fall to the LocalRecipeDataSource via the repository.
        Unit.right()

    override suspend fun saveRecipe(recipe: Recipe): Either<AppError, Unit> =
        // Remote source typically doesn't save individual recipes this way
        AppError.UnsupportedOperation("Saving a single recipe is not supported by RemoteRecipeDataSource")
            .left()

    /**
     * Extract recipe data from a URL
     */
    override suspend fun extractRecipeFromUrl(url: String): Either<AppError, Recipe> =
        try {
            val dto = apiClient.extractRecipeFromUrl(url)
            dto.toDomain().right()
        } catch (e: Exception) {
            when (e) {
                is AuthenticationException -> AppError.UnauthorizedError(
                    e.message ?: "Not authenticated", e
                )

                is ForbiddenException -> AppError.ForbiddenError(e.message ?: "Forbidden")
                is NotFoundException -> AppError.NotFoundError(e.message ?: "Not found")
                is ValidationException -> AppError.ValidationError(
                    e.message ?: "Validation error"

                )

                is SocketTimeoutException -> AppError.NetworkError(
                    "Request timed out while extracting recipe"

                )

                is UnknownHostException, is ConnectException -> AppError.NetworkError(
                    "Cannot connect to server. Please check your internet connection.",
                    e
                )

                else -> AppError.NetworkError(
                    "Failed to extract recipe: ${e.message ?: "Unknown error"}",
                    e
                )
            }.left()
        }
}
