package com.example.fitnessapp.presentation.screens.profile_screen_package.profile_screen

import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_DARK_MODE = "dark_mode"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveDarkMode(context: Context, isDark: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_DARK_MODE, isDark).apply()
    }

    fun getDarkMode(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_DARK_MODE, false)
    }
}
