package com.example.android.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.asteroidradar.api.parseAsteroidsJsonResult
import com.example.android.asteroidradar.models.Asteroid
import com.example.android.asteroidradar.util.Constants
import com.example.android.asteroidradar.models.PictureOfDay
import com.example.android.asteroidradar.database.AsteroidRadarDatabase
import com.example.android.asteroidradar.database.asDomainModel
import com.example.android.asteroidradar.network.Network
import com.example.android.asteroidradar.network.asDatabaseModel
import com.example.android.asteroidradar.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.util.*

class AsteroidRadarRepository(private val database: AsteroidRadarDatabase) {


    val today = Util.convertDateToString(Calendar.getInstance().time, Constants.API_QUERY_DATE_FORMAT)
    val week = Util.convertDateToString(Util.addDaysToDate(Calendar.getInstance().time, 7),
        Constants.API_QUERY_DATE_FORMAT)

    val picture: LiveData<PictureOfDay> =
        Transformations.map(database.pictureOfDayDao.getPictureOfDay()) {
            it?.asDomainModel()
        }


    val asteroidsSaved: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidsDao.getAllAsteroids()) {
            it.asDomainModel()
        }
    val asteroidsWeek: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidsDao.getWeekAsteroids(today,week)) {
            it.asDomainModel()
        }
    val asteroidsToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidsDao.getTodayAsteroids(today)) {
            it.asDomainModel()
        }

    suspend fun refreshPicOfDay() {

        withContext(Dispatchers.IO) {

            try {
                val pic = Network.asteroidRadar.getPictureOfTheDay(Constants.Api_Key).await()
                database.pictureOfDayDao.insertPictureOfDay(pic.asDatabaseModel())
            } catch (e: Exception) {
            }
        }
    }


    suspend fun refreshAsteroids() {

        withContext(Dispatchers.IO) {
            try {

                val asteroids = Network.asteroidRadar.getAsteroids(Constants.Api_Key).await()
                database.asteroidsDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroids)).asDatabaseModel())

            } catch (e: HttpException) {

            }
        }
    }

}