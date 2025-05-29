package com.uriolus.recipes.feature.auth.domain.use_case

import com.uriolus.recipes.core.common.Resource
import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import com.uriolus.recipes.feature.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.junit5.MockKExtension

@ExtendWith(MockKExtension::class)
class LoginUseCaseTest {

    @MockK
    private lateinit var mockAuthRepository: AuthRepository

    private lateinit var loginUseCase: LoginUseCase

    @BeforeEach
    fun setUp() {
        loginUseCase = LoginUseCase(mockAuthRepository)
    }

    @Test
    fun `invoke with valid credentials returns success`() = runTest {
        val username = "testuser"
        val password = "password123"
        val tokenResponse = TokenResponse("test_access_token", "bearer")
        
        coEvery { mockAuthRepository.login(username, password) } returns Resource.Success(tokenResponse)

        val result = loginUseCase(username, password)

        assertTrue(result is Resource.Success)
        assertEquals(tokenResponse, (result as Resource.Success).data)
        coVerify(exactly = 1) { mockAuthRepository.login(username, password) }
    }

    @Test
    fun `invoke with invalid credentials returns failure`() = runTest {
        val username = "wronguser"
        val password = "wrongpassword"
        val errorMessage = "Invalid credentials"
        val exception = RuntimeException(errorMessage)

        coEvery { mockAuthRepository.login(username, password) } returns Resource.Error(errorMessage, null)

        val result = loginUseCase(username, password)

        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
        coVerify(exactly = 1) { mockAuthRepository.login(username, password) }
    }

    @Test
    fun `invoke with blank username returns error`() = runTest {
        val username = ""
        val password = "password123"

        val result = loginUseCase(username, password)

        assertTrue(result is Resource.Error)
        assertEquals("Username and password cannot be empty.", (result as Resource.Error).message)
        coVerify(exactly = 0) { mockAuthRepository.login(any(), any()) }
    }

    @Test
    fun `invoke with blank password returns error`() = runTest {
        val username = "testuser"
        val password = ""

        val result = loginUseCase(username, password)

        assertTrue(result is Resource.Error)
        assertEquals("Username and password cannot be empty.", (result as Resource.Error).message)
        coVerify(exactly = 0) { mockAuthRepository.login(any(), any()) }
    }
}
