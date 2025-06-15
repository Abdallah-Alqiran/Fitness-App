package com.example.fitnessapp.presentation.viewModels.save_userData_viewModel

sealed class SaveUserDataState {
    data object None: SaveUserDataState()
    data object Loading: SaveUserDataState()
    data object Success: SaveUserDataState()
    data class Error(val error: String): SaveUserDataState()

}