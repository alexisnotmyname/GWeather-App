package com.alexc.ph.weatherapp.ui.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexc.ph.domain.model.weather.Coord
import com.alexc.ph.domain.model.weather.Main
import com.alexc.ph.domain.model.weather.Sys
import com.alexc.ph.domain.model.weather.Weather
import com.alexc.ph.domain.model.weather.WeatherInfo
import com.alexc.ph.weatherapp.R
import com.alexc.ph.weatherapp.ui.components.GWeatherTopAppBar
import com.alexc.ph.weatherapp.ui.components.GenericErrorScreen
import com.alexc.ph.weatherapp.ui.components.LoadingScreen
import com.alexc.ph.weatherapp.ui.home.history.WeatherHistoryScreen
import com.alexc.ph.weatherapp.ui.icons.GWeatherIcons

@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var showMessagePermissionDenied by remember { mutableStateOf(false) }
    RequestLocationPermissionUsingRememberLauncherForActivityResult(
        onPermissionGranted = {
            viewModel.fetchWeatherData()
        },
        onPermissionDenied = {
            //show permission denied info
            showMessagePermissionDenied = true
        },
    )

    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    when(uiState) {
        is HomeUiState.Error -> GenericErrorScreen()
        HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Success -> {
            val weatherData = (uiState as HomeUiState.Success).weather
             HomeScreen(
                modifier = Modifier.fillMaxSize(),
                weatherData = weatherData,
                onSignOut = {
                    viewModel.signOut()
                    onSignOut()
                }
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    weatherData: Weather,
    onSignOut: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Current Weather", "History")

    Column(modifier = modifier) {
        GWeatherTopAppBar(
            title = stringResource(R.string.app_name),
            action = {
                IconButton(onClick = onSignOut) {
                    Icon(
                        imageVector = GWeatherIcons.Logout,
                        contentDescription = null
                    )
                }
            }
        )

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> WeatherScreen(
                    modifier = Modifier.fillMaxSize(),
                    weatherData = weatherData
                )
            1 -> WeatherHistoryScreen()
        }
    }
}

@Composable
fun RequestLocationPermissionUsingRememberLauncherForActivityResult(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val arePermissionsGranted = permissionsMap.values.reduce { acc, next ->
            acc && next
        }

        if (arePermissionsGranted) {
            onPermissionGranted.invoke()
        } else {
            onPermissionDenied.invoke()
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockWeatherData = Weather(
        id = 1,
        name = "New York",
        timezone = 3600,
        coord = Coord(
            lat = 40.7128,
            lon = -74.0060
        ),
        main = Main(
            temp = 25.5,
            feelsLike = 24.8,
            tempMin = 23.0,
            tempMax = 27.0,
            pressure = 1012,
            humidity = 60,
            seaLevel = 1012,
            grndLevel = 998
        ),
        sys = Sys(
            country = "US",
            id = 1,
            sunrise = 123,
            sunset = 123,
            type = 1
        ),
        listOf(
            WeatherInfo(
                id = 1,
                main = "Sunny",
                description = "Clear sky",
                icon = "01d"
            )
        )
    )
    HomeScreen(modifier = Modifier.fillMaxSize(), weatherData = mockWeatherData, onSignOut = { })
}