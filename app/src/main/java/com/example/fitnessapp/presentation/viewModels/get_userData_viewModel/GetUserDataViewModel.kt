package com.example.fitnessapp.presentation.viewModels.get_userData_viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.domain.repo.FirebaseRepository
import com.example.fitnessapp.presentation.mapper.toUserInfoUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetUserDataViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val _getUserDataState = MutableStateFlow<GetUserDataState>(GetUserDataState.None)
    val getUserDataState = _getUserDataState.asStateFlow()

    init {
        getUserData()
    }

    fun getUserData() {
        _getUserDataState.value = GetUserDataState.Loading
        viewModelScope.launch {
            try {
                val result = firebaseRepository.getUserData().toUserInfoUiModel()
                _getUserDataState.value = GetUserDataState.Success(result)
            } catch(e: Exception) {
                _getUserDataState.value = GetUserDataState.Error(e.message.toString())
            }
        }
    }
}