package com.alexc.ph.weatherapp

import app.cash.turbine.test
import com.alexc.ph.domain.SignInUseCase
import com.alexc.ph.domain.model.Result
import com.alexc.ph.weatherapp.ui.login.LoginUiState
import com.alexc.ph.weatherapp.ui.login.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel
    @MockK private lateinit var signInUseCase: SignInUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginViewModel = LoginViewModel(signInUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should update state to Success when login successful`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val token = "fake-jwt-token"

        every { signInUseCase(email, password) } returns flowOf(Result.Success(token))

        loginViewModel.login(email, password)

        loginViewModel.loginState.test {
            assertEquals(LoginUiState.Loading, awaitItem())
            assertEquals(LoginUiState.Success(token), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should update state to Error when login fails`() = runTest {
        val exception = Exception("Login fails")
        val email = "test@example.com"
        val password = "password123"

        every { signInUseCase(email, password) } returns flowOf(Result.Error(exception))

        loginViewModel.login(email, password)

        loginViewModel.loginState.test {
            assertEquals(LoginUiState.Loading, awaitItem())
            assertEquals(LoginUiState.Error(exception), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

}