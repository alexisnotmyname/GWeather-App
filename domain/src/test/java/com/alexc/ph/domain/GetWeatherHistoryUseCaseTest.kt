package com.alexc.ph.domain

import app.cash.turbine.test
import com.alexc.ph.domain.model.weather.WeatherHistory
import com.alexc.ph.domain.repository.WeatherRepository
import com.alexc.ph.testutils.MockGenerator
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetWeatherHistoryUseCaseTest {

    private lateinit var getWeatherHistoryUseCase: GetWeatherHistoryUseCase
    @MockK
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getWeatherHistoryUseCase = GetWeatherHistoryUseCase(weatherRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should return weather history from repository`() = runTest {
        val weatherHistory = MockGenerator.generateRandomWeatherHistory()
        every { weatherRepository.getWeatherHistory() } returns flowOf(listOf(weatherHistory))

        getWeatherHistoryUseCase().test {
            val result = awaitItem()
            assert(result == listOf(weatherHistory))
            awaitComplete()
        }

        verify { weatherRepository.getWeatherHistory() }
    }

}