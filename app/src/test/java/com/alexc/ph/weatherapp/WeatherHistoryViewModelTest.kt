package com.alexc.ph.weatherapp

import app.cash.turbine.test
import com.alexc.ph.domain.GetWeatherHistoryUseCase
import com.alexc.ph.domain.model.weather.WeatherHistory
import com.alexc.ph.weatherapp.ui.home.history.WeatherHistoryUiState
import com.alexc.ph.weatherapp.ui.home.history.WeatherHistoryViewModel
import com.alexc.ph.weatherapp.ui.splash.SplashUiState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class WeatherHistoryViewModelTest {

    private lateinit var viewmodel: WeatherHistoryViewModel
    @MockK
    private lateinit var getWeatherHistoryUseCase: GetWeatherHistoryUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewmodel = WeatherHistoryViewModel(getWeatherHistoryUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should update state to Success when weather history is retrieved`() = runTest {

        val mockWeatherHistories = listOf(mockk<WeatherHistory>())
        every { getWeatherHistoryUseCase() } returns flowOf(mockWeatherHistories)

        viewmodel.weatherHistoryUiState.test {
            assertEquals(awaitItem(), WeatherHistoryUiState.Loading)
            assertEquals(awaitItem(), WeatherHistoryUiState.Success(mockWeatherHistories))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getWeatherHistoryUseCase() }
    }

    @Test
    fun `Should update state to Error when exception occurs`() = runTest {
        val exception = Exception("Test exception")
        every { getWeatherHistoryUseCase() } throws exception

        viewmodel.weatherHistoryUiState.test {
            assertEquals(awaitItem(), WeatherHistoryUiState.Loading)
            assertEquals(awaitItem(), WeatherHistoryUiState.Error(exception))
            cancelAndIgnoreRemainingEvents()
        }
    }

}