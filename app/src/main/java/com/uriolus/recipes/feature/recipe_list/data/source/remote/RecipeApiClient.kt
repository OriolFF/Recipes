package com.uriolus.recipes.feature.recipe_list.data.source.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Ktor client for communicating with the recipe backend API
 */
@Singleton
class RecipeApiClient @Inject constructor() {
    
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
    }
    
    private val baseUrl = "http://localhost:8000" // This should be configurable for different environments
    
    /**
     * Get all recipes from the backend
     */
    suspend fun getAllRecipes(): List<RecipeDto> {
        return client.get("$baseUrl/recipes").body()
    }
    
    /**
     * Get a specific recipe by ID
     */
    suspend fun getRecipeById(id: String): RecipeDto {
        return client.get("$baseUrl/recipes/$id").body()
    }
    
    /**
     * Extract recipe data from a URL
     */
    suspend fun extractRecipeFromUrl(url: String): RecipeDto {
        return client.post("$baseUrl/recipes/extract") {
            contentType(ContentType.Application.Json)
            setBody(ExtractRecipeRequest(url))
        }.body()
    }
}
