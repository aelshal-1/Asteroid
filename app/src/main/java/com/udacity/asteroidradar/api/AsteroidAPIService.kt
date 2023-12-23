package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()
interface AsteroidAPIService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("api_key") apiKey:String = Constants.API_KEY) :String


    @GET("planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") apiKey: String = Constants.API_KEY) : PictureOfDay
}

object AsteroidApi{
    val retrofitService : AsteroidAPIService by lazy { retrofit.create(AsteroidAPIService::class.java) }
}