package com.alexc.ph.domain

import com.alexc.ph.domain.repository.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import kotlin.test.Test

class IsAuthenticatedUseCaseTest {

    private lateinit var isAuthenticatedUseCase: IsAuthenticatedUseCase
    @MockK private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        isAuthenticatedUseCase = IsAuthenticatedUseCase(authRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should return true if token is not null`() {
        val token = "test-token"
        every { authRepository.getToken() } returns token

        val result = isAuthenticatedUseCase()
        assert(result)
    }

    @Test
    fun `Should return false if token is null`() {
        every { authRepository.getToken() } returns null

        val result = isAuthenticatedUseCase()
        assert(!result)
    }
}