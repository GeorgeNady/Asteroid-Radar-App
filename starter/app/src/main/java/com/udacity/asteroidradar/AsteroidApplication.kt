package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.worker.RefreshDataWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.Timber.Forest.plant
import java.util.concurrent.TimeUnit


class AsteroidApplication:Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private val workManager by lazy { WorkManager.getInstance(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
        applicationScope.launch {
            workConfiguration()
        }
    }

    private fun workConfiguration() {

        // clean work constraints
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }
            .build()

        // work request
        val refresherRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(
            12, TimeUnit.HOURS
        ).setConstraints(constraint).build()

        workManager.enqueueUniquePeriodicWork(
            REFRESH_DB_WORKER_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            refresherRequest
        )

    }

    private companion object {
        const val REFRESH_DB_WORKER_NAME = "refresherRequest"
    }

}