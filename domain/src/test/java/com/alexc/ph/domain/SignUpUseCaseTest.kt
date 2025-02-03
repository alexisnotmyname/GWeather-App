package com.alexc.ph.domain

import app.cash.turbine.test
import com.alexc.ph.domain.common.InvalidEmailException
import com.alexc.ph.domain.common.PasswordFormatInvalidException
import com.alexc.ph.domain.common.PasswordNotMatchException
import com.alexc.ph.domain.model.Result
import com.alexc.ph.domain.repository.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SignUpUseCaseTest {

    private lateinit var signUpUseCase: SignUpUseCase
    @MockK private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        signUpUseCase = SignUpUseCase(authRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should create account with valid email and matching passwords`() = runTest {
        val email = "test@example.com"
        val password = "Test1234"
        val repeatPassword = "Test1234"

        coEvery { authRepository.createAccount(any(), any()) } returns Unit

        signUpUseCase(email, password, repeatPassword).test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(Unit), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { authRepository.createAccount(email, password) }
    }

    @Test
    fun `Should throw InvalidEmailException for invalid email`() = runTest {
        val email = "invalid-email"
        val password = "Test1234"
        val repeatPassword = "Test1234"

        signUpUseCase(email, password, repeatPassword).test {
            assertEquals(Result.Loading, awaitItem())
            assertTrue((awaitItem() as Result.Error).exception is InvalidEmailException)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 0) { authRepository.createAccount(any(), any()) }
    }

    @Test
    fun `Should throw PasswordFormatInvalidException for invalid password`() = runTest {
        val email = "test@example.com"
        val password = "weak"
        val repeatPassword = "weak"

        signUpUseCase(email, password, repeatPassword).test {
            assertEquals(Result.Loading, awaitItem())
            assertTrue((awaitItem() as Result.Error).exception is PasswordFormatInvalidException)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 0) { authRepository.createAccount(any(), any()) }
    }

    @Test
    fun `Should throw PasswordNotMatchException for non-matching passwords`() = runTest {
        val email = "test@example.com"
        val password = "Test1234"
        val repeatPassword = "DifferentPassword"

        signUpUseCase(email, password, repeatPassword).test {
            assertEquals(Result.Loading, awaitItem())
            assertTrue((awaitItem() as Result.Error).exception is PasswordNotMatchException)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 0) { authRepository.createAccount(any(), any()) }
    }
}