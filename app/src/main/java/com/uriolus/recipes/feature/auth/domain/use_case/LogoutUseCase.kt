package com.uriolus.recipes.feature.auth.domain.use_case

import com.uriolus.recipes.core.data.preferences.TokenStorageManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val tokenStorageManager: TokenStorageManager
) {
    suspend operator fun invoke() {
        tokenStorageManager.clearAccessToken()
    }
}
