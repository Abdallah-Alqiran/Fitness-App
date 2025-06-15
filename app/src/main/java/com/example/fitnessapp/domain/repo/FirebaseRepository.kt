package com.example.fitnessapp.domain.repo

import com.example.fitnessapp.data.datasources.remote.model.FoodCaloriesModel
import com.example.fitnessapp.data.datasources.remote.model.Muscles
import com.example.fitnessapp.data.datasources.remote.model.UserInfoDataModel
import com.example.fitnessapp.presentation.screens.healthy_recipes_screen.model.RecipesModel

interface FirebaseRepository {

    suspend fun getAllMuscleExercises(): List<Muscles>

    suspend fun saveUserData(userData: Map<String, Any>)

    suspend fun getUserData(): UserInfoDataModel?

    suspend fun getAllRecipes(): List<RecipesModel>

    suspend fun getAllFoodsWithCalories(): List<FoodCaloriesModel>

    suspend fun createUserEmailAndPassword(email: String, password: String)
}