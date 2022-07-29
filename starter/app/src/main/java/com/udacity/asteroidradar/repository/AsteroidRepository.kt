package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.WorkManager
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.ImageOfDay
import com.udacity.asteroidradar.network.Network.api
import com.udacity.asteroidradar.utile.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.utile.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.utile.RequestState
import com.udacity.asteroidradar.utile.asDatabaseModel
import com.udacity.asteroidradar.utile.parseAsteroidsJsonResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(
    private val database: AsteroidDatabase
) {

    private val calendar by lazy { Calendar.getInstance() }
    private val dateFormat by lazy { SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault()) }

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map((database.asteroidDao.getAllAsteroids())) {
            it?.let { list ->
                list.map { asteroid -> asteroid.asDomainModel() }
            }
        }

    val imageOfDay: LiveData<ImageOfDay> =
        Transformations.map((database.imageDayDao.getAllImages())) {
            it?.map { img -> img.asDomainModel() }?.first()
        }


    suspend fun refreshAsteroids(): RequestState = withContext(Dispatchers.IO) {
        try {
            val startDate = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, DEFAULT_END_DATE_DAYS)
            val endDate = dateFormat.format(calendar.time)
            val asteroidsResponseBody = api.getNeoFeedAsync(startDate, endDate).await()
            val parsedAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsResponseBody))
            database.asteroidDao.insertAsteroids(*parsedAsteroids.asDatabaseModel())
            RequestState.DONE
        } catch (e: Exception) {
            Timber.e("exception: $e")
            RequestState.FAILED
        }
    }

    suspend fun refreshImageOfDay() : RequestState = withContext(Dispatchers.IO) {
        try {
            val response = api.getImageOfTheDay()
            database.imageDayDao.insertImages(response.asDatabaseModel())
            RequestState.DONE
        } catch (e: Exception) {
            Timber.e("exception: $e")
            RequestState.FAILED
        }
    }

    suspend fun deleteAllEntitiesFromTables() = withContext(Dispatchers.IO) {
        database.asteroidDao.getAllAsteroids()
        database.imageDayDao.deleteAllPictures()
    }

}