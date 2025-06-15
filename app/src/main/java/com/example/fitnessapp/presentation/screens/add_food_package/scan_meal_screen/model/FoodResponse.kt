package com.example.fitnessapp.presentation.screens.add_food_package.scan_meal_screen.model

import kotlinx.serialization.Serializable

@Serializable
data class FoodResponse(
    val items: List<String>,
    val count: Int,
    val success: Boolean
)