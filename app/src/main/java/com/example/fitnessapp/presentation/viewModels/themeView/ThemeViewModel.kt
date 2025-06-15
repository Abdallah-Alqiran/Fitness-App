package com.example.fitnessapp.presentation.viewModels.themeView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.fitnessapp.presentation.screens.profile_screen_package.profile_screen.SharedPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

   private val context = application.applicationContext
   private val _isDarkTheme = MutableStateFlow(SharedPrefs.getDarkMode(context))
   val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

   fun toggleTheme(isDark: Boolean) {
      SharedPrefs.saveDarkMode(context, isDark)
      _isDarkTheme.value = isDark
   }
}
