package com.uriolus.recipes.feature.auth.domain.use_case 

import com.uriolus.recipes.core.data.preferences.TokenStorageManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class LogoutUseCaseTest {

    @MockK
    private lateinit var mockTokenStorageManager: TokenStorageManager

    private lateinit var logoutUseCase: LogoutUseCase

    @BeforeEach
    fun setUp() {
        logoutUseCase = LogoutUseCase(mockTokenStorageManager)
    }

    @Test
    fun `invoke clears access token successfully`() = runTest {
        // Arrange: Mock clearAccessToken to do nothing (return Unit)
        coEvery { mockTokenStorageManager.clearAccessToken() } returns Unit

        // Act: Call the use case
        logoutUseCase()

        // Assert: Verify that clearAccessToken was called exactly once
        coVerify(exactly = 1) { mockTokenStorageManager.clearAccessToken() }
    }

    @Test
    fun `invoke throws exception when clearAccessToken fails`() = runTest {
        // Arrange: Mock clearAccessToken to throw an exception
        val expectedException = RuntimeException("Failed to clear token")
        coEvery { mockTokenStorageManager.clearAccessToken() } throws expectedException

        // Act & Assert: Call the use case and assert that the expected exception is thrown
        val actualException = assertThrows<RuntimeException> {
            logoutUseCase()
        }
        assertEquals(expectedException, actualException)

        // Assert: Verify that clearAccessToken was called exactly once
        coVerify(exactly = 1) { mockTokenStorageManager.clearAccessToken() }
    }
}
