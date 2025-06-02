package com.uriolus.recipes.core.data.remote

/**
 * Custom exceptions for API error signaling.
 */
class NotFoundException(message: String) : Exception(message)
class ForbiddenException(message: String) : Exception(message)
class ValidationException(message: String) : Exception(message)
