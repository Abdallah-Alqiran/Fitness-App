package com.example.fitnessapp.presentation.screens.add_food_package.search_food_and_add_screens.viewModel

import com.example.fitnessapp.data.datasources.remote.model.FoodCaloriesModel

sealed class FoodSearchState {
    data object Loading: FoodSearchState()
    data class Success(val foods: List<FoodCaloriesModel>): FoodSearchState()
    data class Error(val message: String): FoodSearchState()
}