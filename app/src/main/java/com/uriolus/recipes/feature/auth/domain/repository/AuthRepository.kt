package com.uriolus.recipes.feature.auth.domain.repository

import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import com.uriolus.recipes.core.common.Resource // Assuming Resource is in core.common

interface AuthRepository {
    suspend fun login(username: String, password: String): Resource<TokenResponse>
}
