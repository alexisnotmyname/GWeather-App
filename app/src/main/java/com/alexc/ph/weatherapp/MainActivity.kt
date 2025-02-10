package com.alexc.ph.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alexc.ph.weatherapp.ui.theme.GWeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GWeatherAppTheme {
                GWeatherApp()
            }
        }
    }
}
