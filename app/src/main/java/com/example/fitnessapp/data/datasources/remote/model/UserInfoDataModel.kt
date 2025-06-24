package com.example.fitnessapp.data.datasources.remote.model

data class UserInfoDataModel(
    val email: String = "NOT Specified",
    val gender: String = "NOT Specified",
    val goal: String = "NOT Specified",
    val height: String = "NOT Specified",
    val level: String = "NOT Specified",
    val userName: String = "NOT Specified",
    val age: String = "NOT Specified",
    val weight: Int = 0,
)