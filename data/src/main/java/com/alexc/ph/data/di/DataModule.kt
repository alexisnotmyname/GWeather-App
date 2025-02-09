package com.alexc.ph.data.di

import android.content.Context
import androidx.room.Room
import com.alexc.ph.data.BuildConfig
import com.alexc.ph.data.database.WeatherDatabase
import com.alexc.ph.data.database.dao.WeatherHistoryDao
import com.alexc.ph.data.local.SecurePreferences
import com.alexc.ph.data.network.retrofit.WeatherService
import com.alexc.ph.domain.repository.AuthRepository
import com.alexc.ph.data.repository.AuthRepositoryImpl
import com.alexc.ph.data.repository.WeatherRepositoryImpl
import com.alexc.ph.domain.repository.WeatherRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : WeatherDatabase =
        Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideProductDao(database: WeatherDatabase): WeatherHistoryDao {
        return database.weatherHistoryDao()
    }

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(
    ): Call.Factory = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        networkJson: Json,
        okhttpCallFactory: dagger.Lazy<Call.Factory>,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(
        retrofit: Retrofit
    ): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideSecurePreferences(@ApplicationContext context: Context): SecurePreferences = SecurePreferences(context)

    @Singleton
    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, securePreferences: SecurePreferences): AuthRepository = AuthRepositoryImpl(firebaseAuth, securePreferences)

    @Singleton
    @Provides
    fun provideWeatherRepository(weatherService: WeatherService, weatherHistoryDao: WeatherHistoryDao): WeatherRepository = WeatherRepositoryImpl(weatherService, weatherHistoryDao)

}