package com.alexc.ph.data

import app.cash.turbine.test
import com.alexc.ph.data.database.dao.WeatherHistoryDao
import com.alexc.ph.data.database.model.toEntityWeatherHistory
import com.alexc.ph.data.network.model.WeatherResponse
import com.alexc.ph.data.network.model.toDomainWeather
import com.alexc.ph.data.network.retrofit.WeatherService
import com.alexc.ph.data.repository.WeatherRepositoryImpl
import com.alexc.ph.testutils.MockGenerator
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    private lateinit var repository: WeatherRepositoryImpl
    @MockK private lateinit var weatherService: WeatherService
    @MockK private lateinit var weatherHistoryDao: WeatherHistoryDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = WeatherRepositoryImpl(weatherService, weatherHistoryDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getCurrentWeatherData should fetch weather data from the api service`() = runTest {
        val weatherResponse = MockGenerator.generateRandomWeatherResponse()
        val lat = "40.7128"
        val lon = "-74.0060"
        val units = "metric"

        coEvery { weatherService.getCurrentWeatherData(any(), any(), any(), any()) } returns weatherResponse

        repository.getCurrentWeatherData(lat, lon, units).test {
            val result = awaitItem()
            assert(result == weatherResponse.toDomainWeather())
            awaitComplete()
        }

        coVerify { weatherService.getCurrentWeatherData(lat = lat, lon = lon, units = units) }
    }

    @Test
    fun `insertWeatherHistory should insert to weather history dao`() = runTest {
        val mockWeatherHistory = MockGenerator.generateRandomWeatherHistory()
        coEvery { weatherHistoryDao.insertWeatherHistory(any()) } just Runs

        repository.insertWeatherHistory(mockWeatherHistory)

        coVerify { weatherHistoryDao.insertWeatherHistory(mockWeatherHistory.toEntityWeatherHistory()) }
    }

    @Test
    fun `getWeatherHistory should return weather history from dao`() = runTest {
        val mockWeatherHistory = MockGenerator.generateRandomWeatherHistory()

        coEvery { weatherHistoryDao.getAllWeatherHistory() } returns flowOf(listOf(mockWeatherHistory.toEntityWeatherHistory()))

        repository.getWeatherHistory().test {
            val result = awaitItem()
            assert(result == listOf(mockWeatherHistory))
            awaitComplete()
        }

        coVerify { weatherHistoryDao.getAllWeatherHistory() }


    }

}