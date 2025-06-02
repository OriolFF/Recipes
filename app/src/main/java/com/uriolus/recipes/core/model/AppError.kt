package com.uriolus.recipes.core.model

import java.io.IOException

/**
 * Sealed hierarchy describing error types that can occur in the application.
 */
sealed interface AppError {
    val message: String
    val cause: Throwable?
    
    data class NetworkError(
        override val message: String, 
        override val cause: Throwable? = null
    ) : AppError
    
    data class DatabaseError(
        override val message: String, 
        override val cause: Throwable? = null
    ) : AppError
    
    data class ValidationError(
        override val message: String, 
        val field: String? = null,
        override val cause: Throwable? = null
    ) : AppError
    
    data class NotFoundError(
        override val message: String = "Resource not found", 
        val id: String? = null,
        override val cause: Throwable? = null
    ) : AppError
    
    data class UnauthorizedError(
        override val message: String = "Unauthorized",
        override val cause: Throwable? = null
    ) : AppError
    
    data class ForbiddenError(
        override val message: String = "Forbidden",
        override val cause: Throwable? = null
    ) : AppError
    
    data class UnsupportedOperation(
        override val message: String,
        override val cause: Throwable? = null
    ) : AppError
    
    data class UnknownError(
        override val message: String = "An unknown error occurred",
        override val cause: Throwable? = null
    ) : AppError
    
    companion object {
        /**
         * Converts a [Throwable] to an appropriate [AppError].
         */
        fun fromThrowable(throwable: Throwable): AppError {
            return when (throwable) {
                is IOException -> NetworkError(
                    message = throwable.message ?: "Network error occurred",
                    cause = throwable
                )
                is java.lang.UnsupportedOperationException -> UnsupportedOperation(
                    message = throwable.message ?: "Operation not supported",
                    cause = throwable
                )
                is AppError -> throwable
                else -> UnknownError(
                    message = throwable.message ?: "An unknown error occurred",
                    cause = throwable
                )
            }
        }
    }
}
