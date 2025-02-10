package com.alexc.ph.weatherapp

import android.app.Application
import com.alexc.ph.data.di.databaseModule
import com.alexc.ph.data.di.networkModule
import com.alexc.ph.data.di.providerModule
import com.alexc.ph.data.di.repositoryModule
import com.alexc.ph.weatherapp.di.appModule
import com.alexc.ph.weatherapp.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class GWeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@GWeatherApplication)
            modules(
                appModule,
                useCaseModule,
                databaseModule,
                networkModule,
                providerModule,
                repositoryModule,
            )
        }
    }
}