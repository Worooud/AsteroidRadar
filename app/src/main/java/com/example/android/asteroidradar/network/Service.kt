package com.example.android.asteroidradar.network

import com.example.android.asteroidradar.util.Constants.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


enum class AsteroidApiFilter(val value: String) { SHOW_WEEK("week"),SHOW_SAVED("saved"), SHOW_TODAY("today") }


interface AsteroidReaderService {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Deferred<NetworkPictureOfDay>

    @GET(value = "neo/rest/v1/feed")
    fun getAsteroids(@Query("api_key") apiKey: String) : Deferred<String>
}


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()


object Network {
    val asteroidRadar = retrofit.create(AsteroidReaderService::class.java)
}