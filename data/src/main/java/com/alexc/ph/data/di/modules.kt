package com.alexc.ph.data.di

import androidx.room.Room
import com.alexc.ph.data.BuildConfig
import com.alexc.ph.data.database.WeatherDatabase
import com.alexc.ph.data.local.SecurePreferences
import com.alexc.ph.data.network.retrofit.WeatherService
import com.alexc.ph.data.repository.AuthRepositoryImpl
import com.alexc.ph.data.repository.WeatherRepositoryImpl
import com.alexc.ph.domain.repository.AuthRepository
import com.alexc.ph.domain.repository.WeatherRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit

val databaseModule = module {
    single {
        Room.databaseBuilder(
        androidApplication(),
        WeatherDatabase::class.java,
        WeatherDatabase.DATABASE_NAME
    ).build() }

    single { get<WeatherDatabase>().weatherHistoryDao() }
}

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single<Call.Factory> {
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .callFactory { get<Call.Factory>().newCall(it) }
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType()),
            )
            .build()
    }

    single {
        get<Retrofit>().create(WeatherService::class.java)
    }
}

val providerModule = module {
    single<FirebaseAuth> {
        Firebase.auth
    }

    single {
        SecurePreferences(androidApplication())
    }
}

val repositoryModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(get(), get())
    }

    single<WeatherRepository> {
        WeatherRepositoryImpl(get(), get())
    }
}