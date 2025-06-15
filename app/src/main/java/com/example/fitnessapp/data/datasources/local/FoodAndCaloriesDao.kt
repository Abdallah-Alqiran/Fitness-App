package com.example.fitnessapp.data.datasources.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodAndCaloriesDao {
    @Insert
    suspend fun insertFoodAndCalories(foodAndCaloriesLocalModel: FoodAndCaloriesLocalModel)

    @Delete
    suspend fun deleteFoodAndCalories(foodAndCaloriesLocalModel: FoodAndCaloriesLocalModel)

    @Query("SELECT * FROM food_and_calories WHERE substr(date, 1, 10) <= substr(:startDate, 1, 10) AND substr(date, 1, 10) >= substr(:endDate, 1, 10) ORDER BY date DESC")
    suspend fun getFoodAndCaloriesByDate(startDate: String, endDate: String): List<FoodAndCaloriesLocalModel>

    @Query("SELECT * FROM food_and_calories")
    suspend fun getAllFoodAndCalories(): List<FoodAndCaloriesLocalModel>

    @Query("SELECT SUM(calories) FROM food_and_calories WHERE substr(date, 1, 10) == substr(:date, 1, 10)")
    suspend fun getTotalDayCalories(date: String): Double

}