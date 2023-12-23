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
    private val asteroidRepository = AsteroidsRepo(database)


    val asteroids = asteroidRepository.asteroids

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
            asteroidRepository.refreshAsteroids()
            _pictureOfDay.value = asteroidRepository.refreshPictureOfTheDay()
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