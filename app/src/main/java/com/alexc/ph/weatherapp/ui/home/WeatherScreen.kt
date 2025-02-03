package com.alexc.ph.weatherapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexc.ph.domain.model.weather.Coord
import com.alexc.ph.domain.model.weather.Main
import com.alexc.ph.domain.model.weather.Sys
import com.alexc.ph.domain.model.weather.Weather
import com.alexc.ph.domain.model.weather.WeatherInfo
import com.alexc.ph.domain.utils.isEvening
import com.alexc.ph.domain.utils.unixToLocalTime
import com.alexc.ph.weatherapp.R

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    weatherData: Weather
) {
    val city = weatherData.name
    val country = weatherData.sys.country
    val temperature = "${weatherData.main.temp}°C"
    val sunrise = weatherData.sys.sunrise.unixToLocalTime()
    val sunset = weatherData.sys.sunset.unixToLocalTime()
    val weatherMain = weatherData.weather.first().main.lowercase()
    val weatherDescription = weatherData.weather.first().main
    val weatherIcon: Painter = when {
        weatherMain.contains("rain") || weatherMain.contains("thunderstorm") || weatherMain.contains("drizzle") -> painterResource(R.drawable.raining)
        isEvening() -> painterResource(R.drawable.moon)
        else -> painterResource(R.drawable.sun)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "$city, $country", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Image(painter = weatherIcon, contentDescription = "Weather Icon")
//        ContentImage(
//            modifier = Modifier.size(80.dp),
//            imageUrl = "https://openweathermap.org/img/wn/${weatherData.weather.first().icon}@2x.png",
//            contentDescription = weatherData.weather.first().description,
//            contentScale = ContentScale.FillBounds
//        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = weatherDescription, style = MaterialTheme.typography.bodySmall)
        Text(text = "Temperature: $temperature", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Sunrise: $sunrise", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Sunset: $sunset", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
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
    WeatherScreen(weatherData = mockWeatherData)
}