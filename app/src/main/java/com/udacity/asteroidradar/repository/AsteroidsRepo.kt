package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.asDomainModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.utils.getNextWeek
import com.udacity.asteroidradar.utils.getToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber



class AsteroidsRepo(private val database: AsteroidsDatabase) {

    val asteroids :LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroid()){
            it.asDomainModel()
        }

    val asteroidsToday :LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidToday(getToday())){
            it.asDomainModel()
        }

    val asteroidsWeek :LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidWeek(getToday(), getNextWeek())){
            it.asDomainModel()
        }



    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            try {
                val result = AsteroidApi.retrofitService.getAsteroids()
                Timber.i(result)
                val asteroidList = parseAsteroidsJsonResult(JSONObject(result))
                database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
            }catch (e: Exception){
                Timber.e(e.message)
            }


        }
    }

    suspend fun refreshPictureOfTheDay(): PictureOfDay{
        lateinit var pictureOfDay: PictureOfDay
        withContext(Dispatchers.IO){
            try {
            pictureOfDay = AsteroidApi.retrofitService.getPictureOfDay()
            Timber.i(pictureOfDay.url)
            }catch (e: Exception){
                Timber.e(e.message)
                pictureOfDay = getDefaultPictureOfDay()
            }
        }
        return pictureOfDay
    }

    private fun getDefaultPictureOfDay(): PictureOfDay {
        return PictureOfDay(
            title = "Default Title",
            url = "https://example.com/default-image.jpg",
            mediaType ="video"
        )
    }
}