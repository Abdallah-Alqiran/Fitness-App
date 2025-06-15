package com.example.fitnessapp.presentation.screens.add_food_package.search_food_and_add_screens.viewModel

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
class FoodSearchViewModel @Inject constructor(private val foodRepo: FirebaseRepository): ViewModel() {

    private val _foodState = MutableStateFlow<FoodSearchState>(FoodSearchState.Loading)
    val foodState = _foodState.asStateFlow()

    init {
        loadFood()
    }

    fun loadFood() {

        _foodState.value = FoodSearchState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _foodState.value = FoodSearchState.Success(foodRepo.getAllFoodsWithCalories())
            } catch (e: Exception) {
                _foodState.value = FoodSearchState.Error(e.message ?: "Unknown error")
            }

        }
    }
    
}