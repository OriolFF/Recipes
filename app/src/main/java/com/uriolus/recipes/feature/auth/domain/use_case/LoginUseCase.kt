package com.uriolus.recipes.feature.auth.domain.use_case

import com.uriolus.recipes.core.common.Resource
import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import com.uriolus.recipes.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Resource<TokenResponse> {
        if (username.isBlank() || password.isBlank()) {
            return Resource.Error("Username and password cannot be empty.")
        }
        return repository.login(username, password)
    }
}
