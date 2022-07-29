package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository

class RefreshDataWork(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx,params) {

    override suspend fun doWork(): Result {

        val database = AsteroidDatabase.getDatabase(applicationContext)
        val repo = AsteroidRepository(database)

        return try {
            repo.deleteAllEntitiesFromTables()
            repo.refreshImageOfDay()
            repo.refreshAsteroids()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}