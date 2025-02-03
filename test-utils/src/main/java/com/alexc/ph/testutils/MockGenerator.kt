package com.alexc.ph.testutils


import com.alexc.ph.data.network.model.CoordResponse
import com.alexc.ph.data.network.model.MainResponse
import com.alexc.ph.data.network.model.SysResponse
import com.alexc.ph.data.network.model.WeatherInfoResponse
import com.alexc.ph.data.network.model.WeatherResponse
import com.alexc.ph.domain.model.weather.Coord
import com.alexc.ph.domain.model.weather.Main
import com.alexc.ph.domain.model.weather.Sys
import com.alexc.ph.domain.model.weather.Weather
import com.alexc.ph.domain.model.weather.WeatherHistory
import com.alexc.ph.domain.model.weather.WeatherInfo
import kotlin.random.Random

object MockGenerator {

    private fun createRandomCoord(): Coord {
        return Coord(
            lon = Random.nextDouble(-180.0, 180.0),
            lat = Random.nextDouble(-90.0, 90.0)
        )
    }

    private fun createRandomMain(): Main {
        return Main(
            temp = Random.nextDouble(10.0, 35.0),
            feelsLike = Random.nextDouble(10.0, 35.0),
            tempMin = Random.nextDouble(5.0, 15.0),
            tempMax = Random.nextDouble(25.0, 35.0),
            pressure = Random.nextInt(950, 1050),
            humidity = Random.nextInt(30, 100),
            seaLevel = Random.nextInt(950, 1050),
            grndLevel = Random.nextInt(950, 1050)
        )
    }

    private fun createRandomSys(): Sys {
        return Sys(
            type = 1,
            id = Random.nextInt(1, 1000),
            country = listOf("US", "CA", "UK", "AU", "JP").random(),
            sunrise = System.currentTimeMillis() - Random.nextLong(
                0,
                86400000
            ), // Sunrise within last 24 hours
            sunset = System.currentTimeMillis() + Random.nextLong(
                0,
                86400000
            ) // Sunset within next 24 hours
        )
    }

    private fun createRandomWeatherInfo(): WeatherInfo {
        val weatherConditions = listOf(
            "Clear" to "01d",
            "Clouds" to "02d",
            "Rain" to "10d",
            "Snow" to "13d",
            "Thunderstorm" to "11d"
        )
        val (main, icon) = weatherConditions.random()
        return WeatherInfo(
            id = Random.nextInt(200, 800), // Weather condition ID
            main = main,
            description = "$main sky",
            icon = icon
        )
    }

    fun generateRandomWeather(): Weather {
        val cityNames = listOf("London", "New York", "Tokyo", "Sydney", "Los Angeles", "Paris", "Berlin", "Rome", "Madrid", "Toronto")
        return Weather(
            id = Random.nextInt(10000, 99999), // City ID
            name = cityNames.random(),
            timezone = Random.nextInt(-43200, 50400), // Timezone offset in seconds
            coord = createRandomCoord(),
            main = createRandomMain(),
            sys = createRandomSys(),
            weather = listOf(createRandomWeatherInfo()) // List of weather info
        )
    }

    fun generateRandomWeatherResponse(): WeatherResponse {
        val weather = generateRandomWeather()
        return WeatherResponse(
            id = weather.id,
            name = weather.name,
            timezone = weather.timezone,
            coord = CoordResponse(
                lon = weather.coord.lon,
                lat = weather.coord.lat
            ),
            main = MainResponse(
                temp = weather.main.temp,
                humidity = weather.main.humidity,
                pressure = weather.main.pressure,
                feelsLike = weather.main.feelsLike,
                grndLevel = weather.main.grndLevel,
                seaLevel = weather.main.seaLevel,
                tempMax = weather.main.tempMax,
                tempMin = weather.main.tempMin
            ),
            sys = SysResponse(
                type = weather.sys.type,
                id = weather.sys.id,
                country = weather.sys.country,
                sunrise = weather.sys.sunrise,
                sunset = weather.sys.sunset
            ),
            weather = listOf(WeatherInfoResponse(
                id = weather.weather.first().id,
                main = weather.weather.first().main,
                description = weather.weather.first().description,
                icon = weather.weather.first().icon
            ))
        )
    }

    fun generateRandomWeatherHistory(): WeatherHistory {
        val cityNames = listOf("London", "New York", "Tokyo", "Sydney", "Los Angeles", "Paris", "Berlin", "Rome", "Madrid", "Toronto")
        val countryNames = listOf("UK", "USA", "Japan", "Australia", "Canada", "Germany", "France", "Italy", "Spain")
        return WeatherHistory(
            city = cityNames.random(),
            country = countryNames.random(),
            temperature = "${Random.nextDouble(10.0, 35.0)}°C",
            sunrise = System.currentTimeMillis() - Random.nextLong(0, 86400000),
            sunset = System.currentTimeMillis() + Random.nextLong(0, 86400000),
            weatherCondition = "Sunny",
            timeStamp = Random.nextLong()
        )
    }

}