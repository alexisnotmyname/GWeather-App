package com.alexc.ph.weatherapp

import app.cash.turbine.test
import com.alexc.ph.domain.IsAuthenticatedUseCase
import com.alexc.ph.weatherapp.ui.splash.SplashUiState
import com.alexc.ph.weatherapp.ui.splash.SplashViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SplashViewModelTest {

    private lateinit var viewModel: SplashViewModel
    @MockK private lateinit var isAuthenticatedUseCase: IsAuthenticatedUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SplashViewModel(isAuthenticatedUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should update state to NoExistingSession when isAuthenticatedUseCase returns false`() = runTest {
        coEvery { isAuthenticatedUseCase() } returns false
        viewModel.splashUiState.test {
            assertEquals(awaitItem(), SplashUiState.Loading)
            assertEquals(awaitItem(), SplashUiState.NoExistingSession)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should update state to LoggedIn when isAuthenticatedUseCase returns true`() = runTest {
        coEvery { isAuthenticatedUseCase() } returns true
        viewModel.splashUiState.test {
            assertEquals(awaitItem(), SplashUiState.Loading)
            assertEquals(awaitItem(), SplashUiState.LoggedIn)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should update state to Error when exception occurs`() = runTest {
        val textException = Exception("Test exception")
        coEvery { isAuthenticatedUseCase() } throws textException
        viewModel.splashUiState.test {
            assertEquals(awaitItem(), SplashUiState.Loading)
            assertEquals(awaitItem(), SplashUiState.Error(textException))
            cancelAndIgnoreRemainingEvents()
        }
    }
}