package com.example.fitnessapp.presentation.viewModels.foodAndCalories_viewModel

import com.example.fitnessapp.presentation.model.FoodAndCaloriesUIModel

sealed class FoodAndCaloriesState {
    data object None: FoodAndCaloriesState()
    data class SuccessWithData(val foodAndCaloriesUIModel: List<FoodAndCaloriesUIModel>): FoodAndCaloriesState()
    data object Success: FoodAndCaloriesState()
    data object Loading: FoodAndCaloriesState()
    data class Error(val error: String): FoodAndCaloriesState()
}