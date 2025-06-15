package com.example.fitnessapp.presentation.mapper

import com.example.fitnessapp.data.datasources.remote.model.UserInfoDataModel
import com.example.fitnessapp.presentation.model.UserInfoUIModel

fun UserInfoDataModel.toUserInfoUiModel(): UserInfoUIModel {
    return UserInfoUIModel(
        email = this.email,
        gender = this.gender,
        goal = this.goal,
        height = this.height,
        level = this.level,
        userName = this.userName,
        weight = this.weight.toString(),
        age = this.age
    )
}