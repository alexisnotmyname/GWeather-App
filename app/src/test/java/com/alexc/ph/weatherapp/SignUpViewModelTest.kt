package com.alexc.ph.weatherapp

import app.cash.turbine.test
import com.alexc.ph.domain.SignUpUseCase
import com.alexc.ph.domain.common.InvalidEmailException
import com.alexc.ph.domain.model.Result
import com.alexc.ph.weatherapp.ui.signup.SignUpUiState
import com.alexc.ph.weatherapp.ui.signup.SignUpViewModel
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

class SignUpViewModelTest {

    private lateinit var viewModel: SignUpViewModel
    @MockK(relaxed = true)
    private lateinit var signUpUseCase: SignUpUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SignUpViewModel(signUpUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should update state to Error when signup fails`() = runTest {
        val exception = InvalidEmailException()
        val email = "test.co"
        val password = "password123"
        val repeatPassword = "password123"

        every { signUpUseCase(email, password, repeatPassword) } returns flowOf(Result.Error(exception))

        viewModel.signUp(email, password, repeatPassword)

        viewModel.uiState.test {
            assertEquals(SignUpUiState.Loading, awaitItem())
            assertEquals(SignUpUiState.Error(exception), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should update state to Success when signup is successful`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val repeatPassword = "password123"

        every { signUpUseCase(email, password, repeatPassword) } returns flowOf(Result.Success(Unit))

        viewModel.signUp(email, password, repeatPassword)

        viewModel.uiState.test {
            assertEquals(SignUpUiState.Loading, awaitItem())
            assertEquals(SignUpUiState.Success, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}