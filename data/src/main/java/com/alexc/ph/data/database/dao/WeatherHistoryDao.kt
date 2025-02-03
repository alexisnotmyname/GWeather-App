package com.alexc.ph.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexc.ph.data.database.model.WeatherHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherHistory(weatherHistoryEntity: WeatherHistoryEntity)

    @Query("SELECT * FROM weather_history ORDER BY timestamp DESC")
    fun getAllWeatherHistory(): Flow<List<WeatherHistoryEntity>>
}