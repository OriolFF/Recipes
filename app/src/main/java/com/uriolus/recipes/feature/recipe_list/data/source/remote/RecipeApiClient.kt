package com.uriolus.recipes.feature.recipe_list.data.source.remote

import com.uriolus.recipes.core.data.preferences.TokenStorageManager
import com.uriolus.recipes.core.data.remote.AuthenticationException
import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.submitForm
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Ktor client for communicating with the recipe backend API
 */
@Singleton
class RecipeApiClient @Inject constructor(private val tokenStorageManager: TokenStorageManager) {
    
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, request ->
                val clientException = exception as? io.ktor.client.plugins.ClientRequestException ?: return@handleResponseExceptionWithRequest
                val response = clientException.response
                if (response.status == HttpStatusCode.Unauthorized) {
                    // Only clear token and throw if it's not the login request itself
                    if (!request.url.pathSegments.contains("token")) {
                        tokenStorageManager.clearAccessToken()
                        throw AuthenticationException("Session expired or token invalid.")
                    }
                }
            }
        }
    }
    
    private val baseUrl = "http://10.0.2.2:8000" // This should be configurable for different environments
    
    /**
     * Authenticate user and get an access token
     */
    suspend fun login(username: String, password: String): TokenResponse {
        return client.submitForm(
            url = "$baseUrl/token",
            formParameters = Parameters.build {
                append("username", username)
                append("password", password)
            }
        ).body()
    }

    /**
     * Get all recipes from the backend
     */
    suspend fun getAllRecipes(): List<RecipeDto> {
        val token = tokenStorageManager.accessTokenFlow.firstOrNull()
        return client.get("$baseUrl/getallrecipes") {
            token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }.body()
    }

    /**
     * Extract recipe data from a URL
     */
    suspend fun extractRecipeFromUrl(url: String): RecipeDto {
        val token = tokenStorageManager.accessTokenFlow.firstOrNull()
        return client.post("$baseUrl/obtainrecipe") {
            token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
            contentType(ContentType.Application.Json)
            setBody(ExtractRecipeRequest(url))
        }.body()
    }
}
