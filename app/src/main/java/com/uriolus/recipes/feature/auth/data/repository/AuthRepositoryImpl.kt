package com.uriolus.recipes.feature.auth.data.repository

import com.uriolus.recipes.core.common.Resource
import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import com.uriolus.recipes.feature.auth.domain.repository.AuthRepository
import com.uriolus.recipes.feature.recipe_list.data.source.remote.RecipeApiClient // Adjusted import based on RecipeApiClient's location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val recipeApiClient: RecipeApiClient
) : AuthRepository {

    override suspend fun login(username: String, password: String): Resource<TokenResponse> {
        return try {
            val response = recipeApiClient.login(username, password)
            Resource.Success(response)
        } catch (e: IOException) { // Network errors
            Resource.Error("Couldn't reach server. Check your internet connection.")
        } catch (e: Exception) { // Other errors, like Ktor's ResponseException for non-2xx
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}
