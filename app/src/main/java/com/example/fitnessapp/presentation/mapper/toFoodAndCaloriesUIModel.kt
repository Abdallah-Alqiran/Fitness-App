package com.example.fitnessapp.presentation.mapper

import com.example.fitnessapp.data.datasources.local.FoodAndCaloriesLocalModel
import com.example.fitnessapp.presentation.model.FoodAndCaloriesUIModel

fun FoodAndCaloriesLocalModel.toFoodAndCaloriesUIModel(): FoodAndCaloriesUIModel {
    return FoodAndCaloriesUIModel(
        date = this.date,
        foodName = this.foodName,
        calories = this.calories,
        totalAmount = this.totalAmount
    )
}