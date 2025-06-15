package com.example.fitnessapp.presentation.screens.muscle_screen.viewModel

import com.example.fitnessapp.data.datasources.remote.model.Muscles

sealed class MuscleState {
    data object Loading: MuscleState()
    data class Success(val muscles: List<Muscles>): MuscleState()
    data class Error(val message: String): MuscleState()
}