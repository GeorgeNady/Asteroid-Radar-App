package com.udacity.asteroidradar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.db.entities.AsteroidTable
import com.udacity.asteroidradar.db.entities.ImageOfDayTable

@Dao
interface ImageDayDao {

    @Query("SELECT * FROM image_day_table")
    fun getAllImages() : LiveData<List<ImageOfDayTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(vararg images: ImageOfDayTable)

    @Query("DELETE FROM image_day_table")
    fun deleteAllPictures()

}