package com.example.android.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.example.android.asteroidradar.database.getDatabase
import com.example.android.asteroidradar.models.Asteroid
import com.example.android.asteroidradar.network.AsteroidApiFilter
import com.example.android.asteroidradar.repository.AsteroidRadarRepository
import kotlinx.coroutines.launch

class MainViewModel (application: Application) : AndroidViewModel(application){


    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRadarRepository(database)
    private val filter = MutableLiveData(AsteroidApiFilter.SHOW_SAVED)


    init {

        viewModelScope.launch {
            try{
                asteroidRepository.refreshPicOfDay()
                asteroidRepository.refreshAsteroids()
            }catch(e:Exception){}

        }
    }

    val picOfDay = asteroidRepository.picture

    val asteroids = Transformations.switchMap(filter){
        when (it) {
            AsteroidApiFilter.SHOW_TODAY -> asteroidRepository.asteroidsToday
            AsteroidApiFilter.SHOW_WEEK -> asteroidRepository.asteroidsWeek
            else -> asteroidRepository.asteroidsSaved
        }
    }



    fun updateFilter(filter: AsteroidApiFilter) {
        this.filter.value = filter
    }




    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()

    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
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