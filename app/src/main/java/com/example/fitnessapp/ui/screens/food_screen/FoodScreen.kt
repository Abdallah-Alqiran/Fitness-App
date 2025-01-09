package com.example.fitnessapp.ui.screens.food_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fitnessapp.ui.components.TopBar
import com.example.fitnessapp.ui.screens.food_screen.components.FoodBody
import com.example.fitnessapp.ui.theme.FitnessAppTheme

@Composable
fun FoodScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { TopBar(title = "Food") }
    ) {
        FoodBody(Modifier.padding(it))
    }
}


@Preview
@Composable
private fun Prev() {
    FitnessAppTheme {
        FoodScreen()
    }
}