package com.example.fitnessapp.presentation.viewModels.foodAndCalories_viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.data.datasources.local.FoodAndCaloriesLocalModel
import com.example.fitnessapp.data.datasources.local.FoodAndCaloriesDao
import com.example.fitnessapp.presentation.mapper.toFoodAndCaloriesUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.String

@HiltViewModel
class FoodAndCaloriesViewModel @Inject constructor(private val dao: FoodAndCaloriesDao) :
    ViewModel() {

    private val _foodAndCaloriesState =
        MutableStateFlow<FoodAndCaloriesState>(FoodAndCaloriesState.None)
    val foodAndCaloriesState = _foodAndCaloriesState.asStateFlow()

    fun insertFoodAndCalories(foodAndCaloriesLocalModel: FoodAndCaloriesLocalModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _foodAndCaloriesState.value = FoodAndCaloriesState.Loading
            try {
                if (foodAndCaloriesLocalModel.foodName.isEmpty()) {
                    _foodAndCaloriesState.value = FoodAndCaloriesState.Error("There is no data to save")
                }
                else {
                    dao.insertFoodAndCalories(foodAndCaloriesLocalModel)
                    _foodAndCaloriesState.value = FoodAndCaloriesState.Success
                }
            } catch (e: Exception) {
                _foodAndCaloriesState.value =
                    FoodAndCaloriesState.Error(e.suppressedExceptions.toString())
            }
        }
    }

    fun getFoodAndCalories(startDate: String, date: String) {
        _foodAndCaloriesState.value = FoodAndCaloriesState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = dao.getFoodAndCaloriesByDate(startDate, date)
                    .map { it.toFoodAndCaloriesUIModel() }
                _foodAndCaloriesState.value = FoodAndCaloriesState.SuccessWithData(data)
            } catch (e: Exception) {
                _foodAndCaloriesState.value =
                    FoodAndCaloriesState.Error(e.suppressedExceptions.toString())
            }
        }
    }

    fun deleteFoodAndCalorie(foodAndCaloriesLocalModel: FoodAndCaloriesLocalModel, startDate: String, endDate:String) {
        _foodAndCaloriesState.value = FoodAndCaloriesState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.deleteFoodAndCalories(foodAndCaloriesLocalModel)
                val data = dao.getFoodAndCaloriesByDate(startDate, endDate).map { it.toFoodAndCaloriesUIModel() }
                _foodAndCaloriesState.value = FoodAndCaloriesState.SuccessWithData(data)
            } catch (e: Exception) {
                _foodAndCaloriesState.value =
                    FoodAndCaloriesState.Error(e.suppressedExceptions.toString())
            }
        }
    }

    fun resetState() {
        _foodAndCaloriesState.value = FoodAndCaloriesState.None
    }

}