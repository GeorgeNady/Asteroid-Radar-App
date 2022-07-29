package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.os.strictmode.CleartextNetworkViolation
import androidx.lifecycle.*
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.ImageOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.utile.RequestState
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.util.concurrent.TimeUnit

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private val database by lazy { AsteroidDatabase.getDatabase(app.applicationContext) }
    private val repo by lazy { AsteroidRepository(database) }

    // cashed
    private val _imageLoadingRequestState = MutableLiveData<RequestState>()
    private val _asteroidsLoadingRequestState = MutableLiveData<RequestState>()

    // public
    val imageOfTheDay: LiveData<ImageOfDay> get() = repo.imageOfDay
    val imageLoadingRequestState: LiveData<RequestState> get() = _imageLoadingRequestState
    val asteroids: LiveData<List<Asteroid>> get() = repo.asteroids
    val asteroidsLoadingRequestState: LiveData<RequestState> get() = _asteroidsLoadingRequestState

    init {
        refreshImageOfDay()
        refreshAsteroids()
    }

    private fun refreshImageOfDay() = viewModelScope.launch {
        _imageLoadingRequestState.value = RequestState.LOADING
        val result = repo.refreshImageOfDay()
        _imageLoadingRequestState.value =  result
    }

    private fun refreshAsteroids() = viewModelScope.launch {
        _asteroidsLoadingRequestState.value = RequestState.LOADING
        val result = repo.refreshAsteroids()
        _asteroidsLoadingRequestState.value = result
    }

}