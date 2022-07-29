package com.udacity.asteroidradar.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.udacity.asteroidradar.db.entities.AsteroidTable
import com.udacity.asteroidradar.db.entities.ImageOfDayTable
import com.udacity.asteroidradar.utile.Constants.DATABASE_NAME

@Database(
    entities = [
        AsteroidTable::class,
        ImageOfDayTable::class
    ],
    version = 1
)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val imageDayDao: ImageDayDao

    companion object {
        private lateinit var INSTANCE: AsteroidDatabase

        fun getDatabase(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AsteroidDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}

