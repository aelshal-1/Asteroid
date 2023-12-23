package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepo
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

class MainViewModel (application: Application): AndroidViewModel(application) {


    private val database = getDatabase(application)
    private val videosRepository = AsteroidsRepo(database)

    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids : LiveData<List<Asteroid>>
        get() = _asteroids

    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetails
        get() = _navigateToAsteroidDetails
    fun onAsteroidClicked(asteroid:Asteroid){
        _navigateToAsteroidDetails.value = asteroid
    }

    fun onAsteroidDetailsNavigated(){
        _navigateToAsteroidDetails.value=null
    }

    init {
        viewModelScope.launch {
            try {
                var pictureOfDay = AsteroidApi.retrofitService.getPictureOfDay(Constants.API_KEY)
                Timber.i(pictureOfDay.url)
                // pictureOfDay.url ="https://apod.nasa.gov/apod/image/2312/BigDipperMt2_Cullen_1365.jpg"
                _pictureOfDay.value = pictureOfDay


                val result = AsteroidApi.retrofitService.getAsteroids("2023-12-28","2023-12-27","hOyEUcCIgIKCn7j49izOotiMY2vWYpp8kLO5bNiI")
                Timber.i(result)
                _asteroids.value = parseAsteroidsJsonResult(JSONObject(result))
                Timber.i("Size:${(_asteroids.value as ArrayList<Asteroid>).size}")
                if(asteroids.value?.isNotEmpty() == true){
                    Timber.i(asteroids.value!![0].codename)
                }
            }catch (e: Exception){
                Timber.e(e)
            }
        }

    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}