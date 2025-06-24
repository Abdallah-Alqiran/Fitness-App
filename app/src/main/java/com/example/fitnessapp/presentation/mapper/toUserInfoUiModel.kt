package com.example.fitnessapp.presentation.mapper

import com.example.fitnessapp.data.datasources.remote.model.UserInfoDataModel
import com.example.fitnessapp.presentation.model.UserInfoUIModel

fun UserInfoDataModel?.toUserInfoUiModel(): UserInfoUIModel {
    return UserInfoUIModel(
        email = this?.email ?: "NOT Specified",
        gender = this?.gender ?: "NOT Specified",
        goal = this?.goal ?: "NOT Specified",
        height = this?.height ?: "NOT Specified",
        level = this?.level ?: "NOT Specified",
        userName = this?.userName ?: "NOT Specified",
        weight = this?.weight.toString(),
        age = this?.age ?: "NOT Specified",
    )
}