package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber


class AsteroidsRepo(private val database: AsteroidsDatabase) {
//    val asteroids: LiveData<List<Asteroid>> =
//        Transformations.map(database.asteroidDao.getAsteroid()) {
//            it.asDomainModel()
//        }


    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            val result = AsteroidApi.retrofitService.getAsteroids("2023-12-28","2023-12-27","hOyEUcCIgIKCn7j49izOotiMY2vWYpp8kLO5bNiI")
            Timber.i(result)
            val asteroidList = parseAsteroidsJsonResult(JSONObject(result))
          //  database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        }
    }
}