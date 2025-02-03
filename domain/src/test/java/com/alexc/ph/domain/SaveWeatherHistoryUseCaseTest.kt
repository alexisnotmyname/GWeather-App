package com.alexc.ph.domain

import com.alexc.ph.domain.model.weather.toWeatherHistory
import com.alexc.ph.domain.repository.WeatherRepository
import com.alexc.ph.testutils.MockGenerator
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class SaveWeatherHistoryUseCaseTest {

    private lateinit var saveWeatherHistoryUseCase: SaveWeatherHistoryUseCase
    @MockK private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        saveWeatherHistoryUseCase = SaveWeatherHistoryUseCase(weatherRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should save weather history to repository`() = runTest{
        val mockWeather = MockGenerator.generateRandomWeather()

        coEvery { weatherRepository.insertWeatherHistory(any()) } returns Unit

        saveWeatherHistoryUseCase(mockWeather)

        coVerify { weatherRepository.insertWeatherHistory(any()) }

    }
}