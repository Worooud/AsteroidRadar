/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface PictureOfDayDao {
    @Query("select * from DatabasePictureOfDay")
    fun getPictureOfDay(): LiveData<DatabasePictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(picture: DatabasePictureOfDay)
}

@Dao
interface AsteroidsDao {
    @Query("select * from DatabaseAsteroids")
    fun getAllAsteroids() : LiveData<List<DatabaseAsteroids>>

    @Query("SELECT * FROM DatabaseAsteroids obj WHERE obj.closeApproachDate = :todayDate")
    fun getTodayAsteroids(todayDate: String) : LiveData<List<DatabaseAsteroids>>

    @Query("SELECT * FROM DatabaseAsteroids obj WHERE obj.closeApproachDate BETWEEN :startDate  AND :endDate  order by closeApproachDate desc")
    fun getWeekAsteroids(startDate: String, endDate: String) : LiveData<List<DatabaseAsteroids>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroids)
}



@Database(entities = [DatabasePictureOfDay::class,DatabaseAsteroids::class], version = 2)
abstract class AsteroidRadarDatabase : RoomDatabase() {

    abstract val pictureOfDayDao: PictureOfDayDao
    abstract val asteroidsDao:AsteroidsDao

}

private lateinit var INSTANCE: AsteroidRadarDatabase
fun getDatabase(context: Context): AsteroidRadarDatabase {
    synchronized(AsteroidRadarDatabase::class.java) {

    if (!::INSTANCE.isInitialized) {
        INSTANCE = Room.databaseBuilder(context.applicationContext,
            AsteroidRadarDatabase::class.java,
            "asteroidRadar")
            .fallbackToDestructiveMigration()
            .build()
    }
}
    return INSTANCE
}