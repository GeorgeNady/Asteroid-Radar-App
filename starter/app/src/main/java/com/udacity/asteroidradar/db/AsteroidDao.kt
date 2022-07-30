package com.udacity.asteroidradar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.db.entities.AsteroidTable
import com.udacity.asteroidradar.domain.Asteroid

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroid_table ORDER by closeApproachDate ASC")
    fun getAllAsteroids() : LiveData<List<AsteroidTable>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate == :today ORDER by closeApproachDate ASC")
    fun getTodayAsteroids(today: String) : LiveData<List<AsteroidTable>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate >= :week ORDER by closeApproachDate ASC")
    fun getWeekAsteroids(week: String) : LiveData<List<AsteroidTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(vararg asteroids: AsteroidTable)

    @Query("DELETE FROM asteroid_table")
    fun deleteAllAsteroids()

}