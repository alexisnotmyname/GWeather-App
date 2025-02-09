package com.alexc.ph.weatherapp

import app.cash.turbine.test
import com.alexc.ph.domain.SignUpUseCase
import com.alexc.ph.domain.common.InvalidEmailException
import com.alexc.ph.domain.model.Result
import com.alexc.ph.weatherapp.ui.signup.SignUpAction
import com.alexc.ph.weatherapp.ui.signup.SignUpViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class SignUpViewModelTest {

    private lateinit var viewModel: SignUpViewModel
    @MockK(relaxed = true)
    private lateinit var signUpUseCase: SignUpUseCase
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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
    fun `Should update email when email is changed`() = runTest {
        val email = "initial@email.com"

        viewModel.onAction(SignUpAction.OnEmailChange(email))
        viewModel.state.test {
            assertEquals(email, awaitItem().email)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should update password when password is changed`() = runTest {
        val password = "password123"

        viewModel.onAction(SignUpAction.OnPasswordChange(password))
        viewModel.state.test {
            assertEquals(password, awaitItem().password)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should update repeatPassword when repeatPassword is changed`() = runTest {
        val repeatPassword = "password123"

        viewModel.onAction(SignUpAction.OnRepeatPasswordChange(repeatPassword))
        viewModel.state.test {
            assertEquals(repeatPassword, awaitItem().repeatPassword)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should update SignUpstate with error when signup returns error`() = runTest {
        val exception = InvalidEmailException()
        val email = "test.co"
        val password = "password123"
        val repeatPassword = "password123"

        every { signUpUseCase(email, password, repeatPassword) } returns flow {
            emit(Result.Error(exception))
        }

        viewModel.onAction(SignUpAction.OnSignUpClick(email, password, repeatPassword))
        viewModel.state.test {
            assertEquals(exception, awaitItem().error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should update SignUpstate isSuccessSignUp to true when signup is successful`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val repeatPassword = "password123"

        every { signUpUseCase(email, password, repeatPassword) } returns flow {
            emit(Result.Success(Unit))
        }

        viewModel.onAction(SignUpAction.OnSignUpClick(email, password, repeatPassword))

        viewModel.state.test {
            assertTrue(awaitItem().isSuccessSignUp)
            cancelAndIgnoreRemainingEvents()
        }
    }
}