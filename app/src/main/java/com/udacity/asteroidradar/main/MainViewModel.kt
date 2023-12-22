package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

class MainViewModel : ViewModel() {
    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids : LiveData<List<Asteroid>>
        get() = _asteroids

    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay : LiveData<PictureOfDay>
        get() = _pictureOfDay

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
                Timber.e(e.message)
            }
        }

    }
}