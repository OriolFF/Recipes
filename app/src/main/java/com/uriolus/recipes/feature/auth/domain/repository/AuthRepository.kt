package com.uriolus.recipes.feature.auth.domain.repository

import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import arrow.core.Either
import com.uriolus.recipes.core.model.AppError


interface AuthRepository {
    suspend fun login(username: String, password: String): Either<AppError, TokenResponse>
}
