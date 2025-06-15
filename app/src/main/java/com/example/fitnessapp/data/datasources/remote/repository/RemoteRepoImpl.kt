package com.example.fitnessapp.data.datasources.remote.repository

import com.example.fitnessapp.data.datasources.remote.RemoteDataSource
import com.example.fitnessapp.data.datasources.remote.model.FoodCaloriesModel
import com.example.fitnessapp.data.datasources.remote.model.Muscles
import com.example.fitnessapp.data.datasources.remote.model.UserInfoDataModel
import com.example.fitnessapp.domain.repo.FirebaseRepository
import com.example.fitnessapp.presentation.screens.healthy_recipes_screen.model.RecipesModel
import javax.inject.Inject

class RemoteRepoImpl @Inject constructor(private val remoteDataSource: RemoteDataSource): FirebaseRepository{
    override suspend fun getAllMuscleExercises(): List<Muscles> {
        return remoteDataSource.getAllMuscles()
    }


    override suspend fun saveUserData(
        userData: Map<String, Any>
    ) {
        return remoteDataSource.saveUserDataFirestore(userData)
    }

    override suspend fun getUserData(): UserInfoDataModel? {
        return remoteDataSource.getUserDataFires()
    }

    override suspend fun createUserEmailAndPassword(email: String, password: String) {
        return remoteDataSource.createUserEmailAndPassword(email, password)
    }

    override suspend fun getAllRecipes(): List<RecipesModel> {
        return remoteDataSource.getAllRecipes()
    }

    override suspend fun getAllFoodsWithCalories(): List<FoodCaloriesModel> {
        return remoteDataSource.getAllFoodsWithCalories()
    }
}