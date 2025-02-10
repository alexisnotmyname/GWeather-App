package com.alexc.ph.weatherapp.ui.home.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexc.ph.domain.model.weather.WeatherHistory
import com.alexc.ph.domain.utils.formatDate
import com.alexc.ph.domain.utils.unixToLocalTime
import com.alexc.ph.weatherapp.R
import com.alexc.ph.weatherapp.ui.components.GenericErrorScreen
import com.alexc.ph.weatherapp.ui.components.LoadingScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherHistoryScreen(
    viewModel: WeatherHistoryViewModel = koinViewModel()
) {
    val uiState by viewModel.weatherHistoryUiState.collectAsStateWithLifecycle()
    when(uiState) {
        is WeatherHistoryUiState.Error -> GenericErrorScreen()
        WeatherHistoryUiState.Loading -> LoadingScreen()
        is WeatherHistoryUiState.Success -> {
            WeatherHistoryScreen(
                modifier = Modifier.fillMaxSize(),
                weatherHistory = (uiState as WeatherHistoryUiState.Success).weatherHistory
            )
        }
    }

}

@Composable
fun WeatherHistoryScreen(
    modifier: Modifier = Modifier,
    weatherHistory: List<WeatherHistory>
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(stringResource(R.string.weather_history), style = MaterialTheme.typography.headlineSmall)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = weatherHistory, key = { it.id }) { weather ->
                WeatherHistoryItem(weather)
            }
        }
    }
}

@Composable
fun WeatherHistoryItem(weather: WeatherHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📍 ${weather.city}, ${weather.country}", style = MaterialTheme.typography.titleMedium)
            Text("🌡 Temperature: ${weather.temperature}°C")
            Text("☁️ Condition: ${weather.weatherCondition}")
            Text("🌅 Sunrise: ${weather.sunrise.unixToLocalTime()} | 🌆 Sunset: ${weather.sunset.unixToLocalTime()}")
            Text("📅 Date: ${weather.timeStamp.formatDate()}", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherHistoryScreenPreview() {
    val weatherHistory = listOf(
        WeatherHistory(
            id = 1,
            city = "New York",
            country = "US",
            temperature = "25.5",
            weatherCondition = "Sunny",
            sunrise = 1738448617,
            sunset = 1738490114,
            timeStamp = System.currentTimeMillis()
        ),
        WeatherHistory(
            id = 2,
            city = "London",
            country = "UK",
            temperature = "20.0",
            weatherCondition = "Cloudy",
            sunrise = 1738448617,
            sunset = 1738490114,
            timeStamp = System.currentTimeMillis() - 3600000 // One hour ago
        ),
        WeatherHistory(
            id = 3,
            city = "Tokyo",
            country = "JP",
            temperature = "28.5",
            weatherCondition = "Rainy",
            sunrise = 1738448617,
            sunset = 1738490114,
            timeStamp = System.currentTimeMillis() - 7200000 // Two hours ago
        )
    )
    WeatherHistoryScreen(weatherHistory = weatherHistory)
}