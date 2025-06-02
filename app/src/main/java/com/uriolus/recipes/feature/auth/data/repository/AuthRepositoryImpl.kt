package com.uriolus.recipes.feature.auth.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.uriolus.recipes.core.data.remote.AuthenticationException
import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.auth.domain.repository.AuthRepository
import com.uriolus.recipes.feature.recipe_list.data.source.remote.RecipeApiClient
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val recipeApiClient: RecipeApiClient
) : AuthRepository {

    override suspend fun login(username: String, password: String): Either<AppError, TokenResponse> {
        return try {
            val response = recipeApiClient.login(username, password) // This now returns TokenResponse or throws Exception
            response
        } catch (e: AuthenticationException) {
            AppError.UnauthorizedError(e.message ?: "Authentication failed", e).left()
        } catch (e: SocketTimeoutException) {
            AppError.NetworkError("Connection timeout. Please check your internet connection.", e).left()
        } catch (e: UnknownHostException) {
            AppError.NetworkError("Cannot connect to server. Please check your internet connection.", e).left()
        } catch (e: IOException) { // Catch other IOExceptions
            AppError.NetworkError("Network error: ${e.message ?: "Unknown IO error"}", e).left()
        } catch (e: Exception) { // Catch any other generic exceptions
            AppError.UnknownError("Login failed: ${e.message ?: "Unknown error"}", e).left()
        }
    }
}
