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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AsteroidsRepo(private val database: AsteroidsDatabase) {



    val asteroids :LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroid()){
            it.asDomainModel()
        }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            val today = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
            val nextWeek = LocalDateTime.now().plusWeeks(1).format(DateTimeFormatter.ISO_DATE)
            Timber.i("today: ${today}")
            Timber.i("next week: ${nextWeek}")
            try {
                val result = AsteroidApi.retrofitService.getAsteroids(today,nextWeek)
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
            pictureOfDay = AsteroidApi.retrofitService.getPictureOfDay()
            Timber.i(pictureOfDay.url)
        }
        return pictureOfDay
    }
}