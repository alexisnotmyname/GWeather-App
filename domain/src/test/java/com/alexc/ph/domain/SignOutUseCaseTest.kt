package com.alexc.ph.domain

import com.alexc.ph.domain.repository.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import kotlin.test.Test

class SignOutUseCaseTest {

    private lateinit var signOutUseCase: SignOutUseCase
    @MockK private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        signOutUseCase = SignOutUseCase(authRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should call signOut on authRepository`() {
        every { authRepository.signOut() } returns Unit
        signOutUseCase()
        verify { authRepository.signOut() }
    }
}