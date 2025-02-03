package com.alexc.ph.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexc.ph.data.database.dao.WeatherHistoryDao
import com.alexc.ph.data.database.model.WeatherHistoryEntity

@Database(entities = [WeatherHistoryEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "pantrypal_db"
    }

    abstract fun weatherHistoryDao(): WeatherHistoryDao
}