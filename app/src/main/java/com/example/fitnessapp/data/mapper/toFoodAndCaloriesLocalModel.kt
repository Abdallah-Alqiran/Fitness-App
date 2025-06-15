package com.example.fitnessapp.data.mapper

import com.example.fitnessapp.data.datasources.local.FoodAndCaloriesLocalModel
import com.example.fitnessapp.presentation.model.FoodAndCaloriesUIModel

fun FoodAndCaloriesUIModel.toFoodAndCaloriesLocalModel() = FoodAndCaloriesLocalModel(
    date = this.date,
    foodName = this.foodName,
    calories = this.calories,
    totalAmount = this.totalAmount
)