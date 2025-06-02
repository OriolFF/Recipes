package com.uriolus.recipes.feature.recipe_list.data.source.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.uriolus.recipes.core.data.preferences.TokenStorageManager
import com.uriolus.recipes.core.data.remote.AuthenticationException
import com.uriolus.recipes.core.data.remote.ForbiddenException
import com.uriolus.recipes.core.data.remote.NotFoundException
import com.uriolus.recipes.core.data.remote.ValidationException
import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import com.uriolus.recipes.core.model.AppError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import java.io.IOException
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Ktor client for communicating with the recipe backend API
 */
@Singleton
class RecipeApiClient @Inject constructor(
    private val tokenStorageManager: TokenStorageManager
) {
    
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
        expectSuccess = false
        
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                val clientException = exception as? io.ktor.client.plugins.ClientRequestException 
                    ?: return@handleResponseExceptionWithRequest
                    
                val response = clientException.response
                val errorMessage = "HTTP ${response.status.value}: ${response.status.description}"
                
                when (response.status) {
                    HttpStatusCode.Unauthorized -> {
                        tokenStorageManager.clearAccessToken()
                        throw AuthenticationException("Session expired or token invalid")
                    }
                    HttpStatusCode.NotFound -> throw NotFoundException("Requested resource not found")
                    HttpStatusCode.Forbidden -> throw ForbiddenException("You don't have permission to access this resource")
                    HttpStatusCode.BadRequest -> throw ValidationException("Invalid request: $errorMessage")
                    in HttpStatusCode.InternalServerError..HttpStatusCode.GatewayTimeout -> 
                        throw IOException("Server error: $errorMessage")
                    else -> {}
                }
            }
        }
    }
    
    private val baseUrl = "http://10.0.2.2:8000"
    
    /**
     * Authenticate user and get an access token
     */
    suspend fun login(username: String, password: String): Either<AppError, TokenResponse> =
        try {
            client.submitForm(
                url = "$baseUrl/token",
                formParameters = parameters {
                    append("username", username)
                    append("password", password)
                }
            ).body<TokenResponse>().right()
        } catch (e: Exception) {
            when (e) {
                is AuthenticationException -> AppError.UnauthorizedError(e.message ?: "Authentication failed")
                is SocketTimeoutException -> AppError.NetworkError("Connection timeout. Please check your internet connection.", e)
                is UnknownHostException, is ConnectException -> 
                    AppError.NetworkError("Cannot connect to server. Please check your internet connection.", e)
                else -> AppError.NetworkError("Login failed: ${e.message ?: "Unknown error"}", e)
            }.left()
        }

    /**
     * Get all recipes from the backend
     */
    suspend fun getAllRecipes(): Either<AppError, List<RecipeApi>> =
        try {
            val token = tokenStorageManager.accessTokenFlow.firstOrNull()
                ?:  emptyList<RecipeApi>().right()
                
            client.get("$baseUrl/getallrecipes") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<List<RecipeApi>>().right()
        } catch (e: Exception) {
            when (e) {
                is AuthenticationException -> AppError.UnauthorizedError(e.message ?: "Not authenticated")
                is SocketTimeoutException -> AppError.NetworkError("Request timed out. Please try again.", e)
                is UnknownHostException, is ConnectException -> 
                    AppError.NetworkError("Cannot connect to server. Please check your internet connection.", e)
                else -> AppError.NetworkError("Failed to fetch recipes: ${e.message ?: "Unknown error"}", e)
            }.left()
        }

    /**
     * Extract recipe data from a URL
     */
    @Throws(Exception::class)
    suspend fun extractRecipeFromUrl(url: String): RecipeApi {
        val token = tokenStorageManager.accessTokenFlow.firstOrNull()
            ?: throw AuthenticationException("Not authenticated")
        return client.post("$baseUrl/obtainrecipe") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(ExtractRecipeRequest(url))
        }.body()
    }
}
