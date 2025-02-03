package com.alexc.ph.domain

import app.cash.turbine.test
import com.alexc.ph.domain.common.InvalidEmailException
import com.alexc.ph.domain.common.PasswordBlankException
import com.alexc.ph.domain.model.Result
import com.alexc.ph.domain.repository.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SIgnInUseCaseTest {

    private lateinit var signInUseCase: SignInUseCase
    @MockK
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        signInUseCase = SignInUseCase(authRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should saveToken on successful login`() = runTest {
        val email = "test@example.com"
        val password = "Test1234"
        val token = "test-token"

        coEvery { authRepository.authenticate(email, password) } returns Result.Success(token)
        coEvery { authRepository.saveToken(token) } returns token

        signInUseCase(email, password).test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(token), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { authRepository.saveToken(token) }
    }

    @Test
    fun `Should throw InvalidEmailException for invalid email`() = runTest {
        val email = "invalid-email"
        val password = "Test1234"

        signInUseCase(email, password).test {
            assertEquals(Result.Loading, awaitItem())
            assertTrue((awaitItem() as Result.Error).exception is InvalidEmailException)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 0) { authRepository.authenticate(any(), any()) }
        coVerify(exactly = 0) { authRepository.saveToken(any()) }
    }

    @Test
    fun `Should throw PasswordBlankException for blank password`() = runTest {
        val email = "test@example.com"
        val password = ""

        signInUseCase(email, password).test {
            assertEquals(Result.Loading, awaitItem())
            assertTrue((awaitItem() as Result.Error).exception is PasswordBlankException)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 0) { authRepository.authenticate(any(), any()) }
        coVerify(exactly = 0) { authRepository.saveToken(any()) }
    }
}