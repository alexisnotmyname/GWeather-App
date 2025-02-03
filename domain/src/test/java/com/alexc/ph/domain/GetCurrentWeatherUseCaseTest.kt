package com.alexc.ph.domain

import app.cash.turbine.test
import com.alexc.ph.domain.model.Result
import com.alexc.ph.domain.model.weather.Coord
import com.alexc.ph.domain.model.weather.Main
import com.alexc.ph.domain.model.weather.Sys
import com.alexc.ph.domain.model.weather.Weather
import com.alexc.ph.domain.model.weather.WeatherInfo
import com.alexc.ph.domain.repository.WeatherRepository
import com.alexc.ph.testutils.MockGenerator
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCurrentWeatherUseCaseTest {

    private lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase
    @MockK private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should return weather data on success response`() = runTest {
        val mockWeatherData = MockGenerator.generateRandomWeather()
        val lat = "123"
        val lon = "456"
        val units = "metric"

        coEvery { weatherRepository.getCurrentWeatherData(any(), any(), any()) } returns flowOf(mockWeatherData)

        getCurrentWeatherUseCase(lat, lon, units).test {
            assertEquals(Result.Loading, awaitItem())
            assert((awaitItem() as Result.Success).data == mockWeatherData)
            awaitComplete()
        }

        coVerify { weatherRepository.getCurrentWeatherData(lat, lon, units) }
    }

    @Test
    fun `Should catch errors when fetching weather data`() = runTest {
        val exception = Exception("Error fetching weather data")
        val lat = "123"
        val lon = "456"
        val units = "metric"

        coEvery { weatherRepository.getCurrentWeatherData(any(), any(), any()) } throws exception

        getCurrentWeatherUseCase(lat, lon, units).test {
            assertEquals(Result.Loading, awaitItem())
            assertTrue((awaitItem() as Result.Error).exception is Exception)
            cancelAndIgnoreRemainingEvents()
        }
    }
}