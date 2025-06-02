package com.uriolus.recipes.feature.auth.domain.use_case

import arrow.core.Either
import arrow.core.left
import com.uriolus.recipes.core.common.Resource
import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import com.uriolus.recipes.core.model.AppError
import com.uriolus.recipes.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Either<AppError, TokenResponse> {
        if (username.isBlank() || password.isBlank()) {
            return AppError.ValidationError("Username and password cannot be empty.").left()
        }
        return repository.login(username, password)
    }
}
