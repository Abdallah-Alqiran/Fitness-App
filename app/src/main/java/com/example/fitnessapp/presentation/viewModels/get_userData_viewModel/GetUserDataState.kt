package com.example.fitnessapp.presentation.viewModels.get_userData_viewModel

import com.example.fitnessapp.presentation.model.UserInfoUIModel


sealed class GetUserDataState {
    data object None: GetUserDataState()
    data object Loading: GetUserDataState()
    data class Success(val userInfo: UserInfoUIModel): GetUserDataState()
    data class Error(val error: String): GetUserDataState()
}