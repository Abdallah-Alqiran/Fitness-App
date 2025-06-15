package com.example.fitnessapp.data.datasources.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class FoodAndCaloriesDatabaseTest {

    @get:Rule
    val instant = InstantTaskExecutorRule()

    private lateinit var database: FoodAndCaloriesDatabase
    private lateinit var dao: FoodAndCaloriesDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FoodAndCaloriesDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.foodAndCalorieDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTest() = runBlocking {
        val foodItem = FoodAndCaloriesLocalModel(
            date = "2025-06-02 55:55",
            foodName = "Koshari",
            totalAmount = 3,
            calories = 120.3
        )
        dao.insertFoodAndCalories(foodItem)
        val allFoods = dao.getFoodAndCaloriesByDate("2025-06-30 55:55", "2025-06-01 55:55")
        assertThat(allFoods).contains(foodItem)
    }


    @Test
    fun deleteTest() = runBlocking {
        val foodItem = FoodAndCaloriesLocalModel(
            date = "2025-06-02 55:55",
            foodName = "Koshari",
            totalAmount = 3,
            calories = 120.3
        )

        dao.insertFoodAndCalories(foodItem)
        dao.insertFoodAndCalories(foodItem.copy(date = "2025-07-02 55:55"))
        dao.deleteFoodAndCalories(foodItem)
        val allFoods = dao.getFoodAndCaloriesByDate("2025-06-30 55:55", "2025-06-01 55:55")
        assertThat(allFoods).doesNotContain(foodItem)
    }

    @Test
    fun getTotalCal() = runBlocking {
        val foodItem = FoodAndCaloriesLocalModel(
            date = "2025-06-02 55:55",
            foodName = "Foooool",
            totalAmount = 3,
            calories = 120.3
        )
        dao.insertFoodAndCalories(foodItem)
        dao.insertFoodAndCalories(foodItem.copy(calories = 987.7, date = "2025-06-02 50:54"))
        val totalCal = dao.getTotalDayCalories("2025-06-02 55:55")
        assertThat(totalCal).isEqualTo(987.7 + 120.3)
    }
}