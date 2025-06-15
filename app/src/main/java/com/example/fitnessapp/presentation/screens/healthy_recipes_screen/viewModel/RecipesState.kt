package com.example.fitnessapp.presentation.screens.healthy_recipes_screen.viewModel

import com.example.fitnessapp.presentation.screens.healthy_recipes_screen.model.RecipesModel

sealed class RecipesState {
    data object Loading: RecipesState()
    data class Success(val recipes: List<RecipesModel>): RecipesState()
    data class Error(val message: String): RecipesState()
}