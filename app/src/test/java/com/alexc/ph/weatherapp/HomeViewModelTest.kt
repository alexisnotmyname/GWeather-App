package com.alexc.ph.weatherapp

import android.location.Location
import app.cash.turbine.test
import com.alexc.ph.domain.GetCurrentWeatherUseCase
import com.alexc.ph.domain.SaveWeatherHistoryUseCase
import com.alexc.ph.domain.SignOutUseCase
import com.alexc.ph.domain.model.Result
import com.alexc.ph.domain.model.weather.Weather
import com.alexc.ph.weatherapp.ui.home.HomeUiState
import com.alexc.ph.weatherapp.ui.home.HomeViewModel
import com.alexc.ph.weatherapp.utils.LocationProvider
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    @MockK
    private lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase
    @MockK(relaxed = true)
    private lateinit var signOutUseCase: SignOutUseCase
    @MockK(relaxed = true)
    private lateinit var saveWeatherHistoryUseCase: SaveWeatherHistoryUseCase
    @MockK
    private lateinit var locationProvider: LocationProvider

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(getCurrentWeatherUseCase, signOutUseCase, saveWeatherHistoryUseCase, locationProvider)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should call signOutUseCase when signOut is called`() {
        viewModel.signOut()

        verify { signOutUseCase() }
    }

    @Test
    fun `Should update state with Success when weather is retrieved`() = runTest {
        val lat = "10.0"
        val long = "20.0"

        val mockLocation = mockk<Location> {
            every { latitude } returns 10.0
            every { longitude } returns 20.0
        }
        val mockWeather = mockk<Weather>()
        val successFlow = flowOf(Result.Success(mockWeather))

        coEvery { locationProvider.getLastUserLocation() } returns mockLocation
        every { getCurrentWeatherUseCase(lat, long, "metric") } returns successFlow
        coEvery { saveWeatherHistoryUseCase(mockWeather) } just Runs

        viewModel.fetchWeatherData()

        viewModel.homeUiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            assertEquals(HomeUiState.Success(mockWeather), awaitItem())
        }

        coVerify { locationProvider.getLastUserLocation() }
        verify { getCurrentWeatherUseCase(lat, long, "metric") }
        coVerify { saveWeatherHistoryUseCase(mockWeather) }
    }

    @Test
    fun `Should update state with Error when weather API fails`() = runTest {
        val lat = "10.0"
        val long = "20.0"

        val exception = Exception("Network error")
        val mockLocation = mockk<Location> {
            every { latitude } returns 10.0
            every { longitude } returns 20.0
        }
        val mockWeather = mockk<Weather>()

        coEvery { locationProvider.getLastUserLocation() } returns mockLocation
        every { getCurrentWeatherUseCase(lat, long, "metric") } returns flowOf(Result.Error(exception))
        coEvery { saveWeatherHistoryUseCase(mockWeather) } just Runs

        viewModel.fetchWeatherData()

        viewModel.homeUiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            assertEquals(HomeUiState.Error(exception), awaitItem())
        }

        coVerify { getCurrentWeatherUseCase(lat, long, "metric") }
    }

    @Test
    fun `Should getCurrentLocation when getLastUserLocation returns null`() = runTest {
        val lat = "10.0"
        val long = "20.0"
        val mockCurrentLocation = mockk<Location> {
            every { latitude } returns 10.0
            every { longitude } returns 20.0
        }
        val mockWeather = mockk<Weather>()
        val successFlow = flowOf(Result.Success(mockWeather))
        coEvery { locationProvider.getLastUserLocation() } returns null
        coEvery { locationProvider.getCurrentLocation() } returns mockCurrentLocation
        every { getCurrentWeatherUseCase(lat, long, "metric") } returns successFlow
        coEvery { saveWeatherHistoryUseCase(mockWeather) } just Runs

        viewModel.fetchWeatherData()

        viewModel.homeUiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            assertEquals(HomeUiState.Success(mockWeather), awaitItem())
        }

        coVerify { locationProvider.getCurrentLocation() }
        verify { getCurrentWeatherUseCase(lat, long, "metric") }
        coVerify { saveWeatherHistoryUseCase(mockWeather) }
    }
}