package com.example.fitnessapp.presentation.screens.healthy_recipes_screen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.domain.repo.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthyRecipesViewModel @Inject constructor(private val healthyRecipes: FirebaseRepository) :
    ViewModel() {

    private val _healthyRecipesState = MutableStateFlow<RecipesState>(RecipesState.Loading)
    val healthyRecipesState = _healthyRecipesState.asStateFlow()

    init {
        loadRecipes()
    }

    fun loadRecipes() {

        _healthyRecipesState.value = RecipesState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _healthyRecipesState.value = RecipesState.Success(healthyRecipes.getAllRecipes())
            } catch (e: Exception) {
                _healthyRecipesState.value = RecipesState.Error(e.message ?: "Unknown error")
            }

        }
    }
}