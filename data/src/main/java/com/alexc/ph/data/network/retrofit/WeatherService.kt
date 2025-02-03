package com.alexc.ph.data.network.retrofit

import com.alexc.ph.data.BuildConfig
import com.alexc.ph.data.network.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeatherData(
        @Query("appid") appid: String = BuildConfig.API_KEY,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String
    ): WeatherResponse
}