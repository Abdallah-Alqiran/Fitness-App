package com.example.fitnessapp.presentation.viewModels.save_userData_viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.domain.repo.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveUserDataViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _saveUserDataState = MutableStateFlow<SaveUserDataState>(SaveUserDataState.None)
    val userDataState = _saveUserDataState.asStateFlow()

    fun saveDataToFirestore(
        data: Map<String, Any>
    ) {
        _saveUserDataState.value = SaveUserDataState.Loading
        try {
            viewModelScope.launch(Dispatchers.IO) {
                firebaseRepository.saveUserData(data)
                _saveUserDataState.value = SaveUserDataState.Success
                Log.d("Al-qiran", "Data saved Successfully from viewModel")
            }
        } catch (e: Exception) {
            Log.d("Al-qiran", "Failed from viewModel")
            _saveUserDataState.value = SaveUserDataState.Error(e.message.toString())
        }
    }

    fun resetUserDataState() {
        _saveUserDataState.value = SaveUserDataState.None
    }

}