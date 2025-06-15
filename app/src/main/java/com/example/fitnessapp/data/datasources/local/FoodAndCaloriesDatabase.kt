package com.example.fitnessapp.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FoodAndCaloriesLocalModel::class], exportSchema = false, version = 1)
abstract class FoodAndCaloriesDatabase: RoomDatabase() {
    abstract fun foodAndCalorieDao(): FoodAndCaloriesDao
}