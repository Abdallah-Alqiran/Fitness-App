package com.example.fitnessapp.data.datasources.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fitnessapp.constants.Constants.Companion.DATE_FORMAT
import com.example.fitnessapp.constants.Constants.Companion.FOOD_AND_CALORIES_DATABASE
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = FOOD_AND_CALORIES_DATABASE)
data class FoodAndCaloriesLocalModel(
    @PrimaryKey(autoGenerate = false)
    val date: String = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(Date()),
    val foodName: String = "",
    val totalAmount: Int = 0,
    val calories: Double = 0.0
)
