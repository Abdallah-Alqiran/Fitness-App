package com.example.fitnessapp.presentation.viewModels.auth_viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth): ViewModel() {
    val currentUser  get() = firebaseAuth.currentUser

    fun signOut() {
        firebaseAuth.signOut()
    }
}