package com.uriolus.recipes.core.data.remote

/**
 * Exception thrown when an API request fails due to authentication issues (e.g., 401 Unauthorized).
 */
class AuthenticationException(message: String = "Authentication failed. Please log in again.") : Exception(message)
