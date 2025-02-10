package com.alexc.ph.weatherapp.di

import com.alexc.ph.domain.GetCurrentWeatherUseCase
import com.alexc.ph.domain.GetWeatherHistoryUseCase
import com.alexc.ph.domain.IsAuthenticatedUseCase
import com.alexc.ph.domain.SaveWeatherHistoryUseCase
import com.alexc.ph.domain.SignInUseCase
import com.alexc.ph.domain.SignOutUseCase
import com.alexc.ph.domain.SignUpUseCase
import com.alexc.ph.weatherapp.ui.home.HomeViewModel
import com.alexc.ph.weatherapp.ui.home.history.WeatherHistoryViewModel
import com.alexc.ph.weatherapp.ui.login.LoginViewModel
import com.alexc.ph.weatherapp.ui.signup.SignUpViewModel
import com.alexc.ph.weatherapp.ui.splash.SplashViewModel
import com.alexc.ph.weatherapp.utils.LocationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val useCaseModule =  module {
    single { GetCurrentWeatherUseCase(get()) }
    single { GetWeatherHistoryUseCase(get()) }
    single { IsAuthenticatedUseCase(get()) }
    single { SaveWeatherHistoryUseCase(get()) }
    single { SignInUseCase(get()) }
    single { SignOutUseCase(get()) }
    single { SignUpUseCase(get()) }
}

val appModule = module {

    viewModelOf(::SplashViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::WeatherHistoryViewModel)

//    viewModel { SplashViewModel(get()) }
//    viewModel { SignUpViewModel(get()) }
//    viewModel { LoginViewModel(get()) }
//    viewModel { HomeViewModel(get(), get(), get(), get()) }
//    viewModel { WeatherHistoryViewModel(get()) }

    single {
        LocationProvider(androidContext())
    }
}