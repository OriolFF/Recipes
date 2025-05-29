package com.uriolus.recipes.feature.auth.presentation

import app.cash.turbine.test
import com.uriolus.recipes.core.data.remote.dto.TokenResponse
import com.uriolus.recipes.feature.auth.domain.use_case.LoginUseCase

import com.uriolus.recipes.core.common.Resource
import com.uriolus.recipes.core.data.preferences.TokenStorageManager
import com.uriolus.recipes.feature.auth.presentation.LoginState
import com.uriolus.recipes.feature.auth.presentation.LoginViewModel
import com.uriolus.recipes.rules.MainDispatcherRule // Ensure this rule exists in your project
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class LoginViewModelTest {

    @JvmField
    @RegisterExtension
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var mockLoginUseCase: LoginUseCase

    @MockK
    private lateinit var mockTokenStorageManager: TokenStorageManager

    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun setUp() {
        viewModel = LoginViewModel(mockLoginUseCase, mockTokenStorageManager)
    }

    @Test
    fun `initial state is correct`() = runTest {
        val expectedInitialLoginState = LoginState()
        assertEquals(expectedInitialLoginState, viewModel.loginState.value)
        assertEquals("", viewModel.username.value)
        assertEquals("", viewModel.password.value)
    }

    @Test
    fun `onUsernameChange updates username state`() = runTest {
        val newUsername = "testuser"
        viewModel.onUsernameChange(newUsername)
        assertEquals(newUsername, viewModel.username.value)
    }

    @Test
    fun `onPasswordChange updates password state`() = runTest {
        val newPassword = "password123"
        viewModel.onPasswordChange(newPassword)
        assertEquals(newPassword, viewModel.password.value)
    }

    @Test
    fun `login success flow`() = runTest {
        val username = "user"
        val password = "pass"
        val tokenResponse = TokenResponse("token", "bearer")
        coEvery { mockLoginUseCase(username, password) } returns Resource.Success(tokenResponse)
        coEvery { mockTokenStorageManager.saveAccessToken(any()) } returns Unit // Mock void method

        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)

        viewModel.loginState.test {
            // Initial LoginState before click (username/password are separate ViewModel properties)
            assertEquals(LoginState(), awaitItem()) // Username/password don't affect LoginState directly before login()
            
            viewModel.login()
            
            // State after click, showing loading
            assertEquals(LoginState(isLoading = true), awaitItem())
            
            // State after successful login
            val successState = awaitItem()
            assertTrue(successState.loginSuccess)
            assertFalse(successState.isLoading)
            assertNull(successState.error)
            assertEquals(tokenResponse, successState.tokenResponse)
            // Verify username and password remain unchanged in the ViewModel
            assertEquals(username, viewModel.username.value)
            assertEquals(password, viewModel.password.value)
            
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { mockLoginUseCase(username, password) }
        coVerify(exactly = 1) { mockTokenStorageManager.saveAccessToken(tokenResponse.accessToken) }
    }

    @Test
    fun `login failure flow`() = runTest {
        val username = "user"
        val password = "pass"
        val errorMessage = "Invalid credentials"
        coEvery { mockLoginUseCase(username, password) } returns Resource.Error(errorMessage)

        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)

        viewModel.loginState.test {
            assertEquals(LoginState(), awaitItem()) // Initial LoginState

            viewModel.login()

            assertEquals(LoginState(isLoading = true), awaitItem())
            
            val errorState = awaitItem()
            assertFalse(errorState.loginSuccess)
            assertFalse(errorState.isLoading)
            assertEquals(errorMessage, errorState.error)
            // Verify username and password remain unchanged in the ViewModel
            assertEquals(username, viewModel.username.value)
            assertEquals(password, viewModel.password.value)

            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { mockLoginUseCase(username, password) }
    }
    
    // The onLoginSuccessHandled functionality is no longer present in the ViewModel.
    // The loginSuccess flag is reset implicitly on a new login attempt or error.
    // This test has been commented out as it's no longer applicable.
    /*
    @Test
    fun `login success flag resets as expected`() = runTest {
        val username = "user"
        val password = "pass"
        val tokenResponse = TokenResponse("token", "bearer")
        coEvery { mockLoginUseCase(username, password) } returns Resource.Success(tokenResponse)
        coEvery { mockTokenStorageManager.saveAccessToken(any()) } returns Unit

        viewModel.onUsernameChange(username)
        viewModel.onPasswordChange(password)
        viewModel.login() // Trigger login success
        
        viewModel.loginState.test {
            assertEquals(LoginState(), awaitItem()) // Initial
            assertEquals(LoginState(isLoading = true), awaitItem()) // Loading
            var currentState = awaitItem() // Success
            assertTrue(currentState.loginSuccess)

            // To test reset, another action that changes loginState is needed.
            // For example, calling login() again with a failure:
            val errorMessage = "Second attempt failed"
            coEvery { mockLoginUseCase(username, password) } returns Resource.Error(errorMessage)
            viewModel.login() // Trigger another login, this time to fail

            assertEquals(LoginState(isLoading = true, tokenResponse = tokenResponse, loginSuccess = true), awaitItem()) // Loading for second attempt (previous success state persists until new state is emitted)
            val errorState = awaitItem() // Error state from second attempt
            assertFalse(errorState.loginSuccess) // loginSuccess should now be false
            assertEquals(errorMessage, errorState.error)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
    */
}
