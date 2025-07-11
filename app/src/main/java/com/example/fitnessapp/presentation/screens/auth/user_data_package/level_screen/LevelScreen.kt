package com.example.fitnessapp.presentation.screens.auth.user_data_package.level_screen

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.fitnessapp.presentation.screens.auth.user_data_package.level_screen.components.LevelContent
import com.example.fitnessapp.presentation.screens.auth.user_data_package.level_screen.models.LevelList
import com.example.fitnessapp.R
import com.example.fitnessapp.theme.FitnessAppTheme


@SuppressLint("MutableCollectionMutableState")
@Composable
fun PhysicalActivityLevel(onPersonLevel: () -> Unit, onBack:() -> Unit = {}) {

    val levelList by remember {
        mutableStateOf(
            mutableListOf(
                LevelList(
                    levelName = "Beginner",
                    levelImage = R.drawable.ex_stretching,
                ),
                LevelList(
                    levelName = "Intermediate",
                    levelImage = R.drawable.ex_running,
                ),
                LevelList(
                    levelName = "Advanced",
                    levelImage = R.drawable.ex_exercise,
                )
            )
        )
    }

    LevelContent(onPersonLevel, levelList, onBack)
}


@Preview
@Composable
fun Prev() {
    FitnessAppTheme {
        PhysicalActivityLevel(onPersonLevel = { })
    }
}