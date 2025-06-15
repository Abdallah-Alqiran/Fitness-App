package com.example.fitnessapp.presentation.model

data class FoodAndCaloriesUIModel(
    val date: String,
    val foodName: String = "",
    val totalAmount: Int = 0,
    val calories: Double = 0.0
)