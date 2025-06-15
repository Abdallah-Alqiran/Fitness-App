package com.example.fitnessapp.presentation.screens.food_history_screen

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.fitnessapp.R
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.data.mapper.toFoodAndCaloriesLocalModel
import com.example.fitnessapp.presentation.model.FoodAndCaloriesUIModel
import com.example.fitnessapp.presentation.viewModels.foodAndCalories_viewModel.FoodAndCaloriesState
import com.example.fitnessapp.presentation.viewModels.foodAndCalories_viewModel.FoodAndCaloriesViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@Composable
fun FoodHistoryScreen() {
    val context = LocalContext.current
    val currentDate = LocalDate.now()

    val coroutineScope = rememberCoroutineScope()

    val foodAndCaloriesViewModel = hiltViewModel<FoodAndCaloriesViewModel>()
    val foodAndCaloriesState =
        foodAndCaloriesViewModel.foodAndCaloriesState.collectAsStateWithLifecycle()

    val endDate = remember {
        mutableStateOf(
            SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS",
                Locale.ENGLISH
            ).format(Date())
        )
    }
    val startDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH).format(Date())

    val deleteFood: (FoodAndCaloriesUIModel) -> Unit = { food ->
        coroutineScope.launch {
            foodAndCaloriesViewModel.deleteFoodAndCalorie(food.toFoodAndCaloriesLocalModel(), startDate, endDate.value)
        }
    }



    LaunchedEffect(endDate.value) {
        foodAndCaloriesViewModel.getFoodAndCalories(startDate, endDate.value)
    }

    Column {
        Button(onClick = {
            val datePicker = DatePickerDialog(
                context,
                { _, year, month, day ->
                    val formattedMonth = String.format(Locale.ENGLISH, "%02d", month + 1)
                    val formattedDay = String.format(Locale.ENGLISH, "%02d", day)
                    val selectedDate = "$year-$formattedMonth-$formattedDay 23:59:59.999"
                    endDate.value = selectedDate
                },
                currentDate.year, currentDate.monthValue - 1, currentDate.dayOfMonth
            )
            datePicker.show()

        },modifier = Modifier.padding(start = 16.dp)) {
            Text("Pick a Date", style = MaterialTheme.typography.titleLarge)
        }

        when (foodAndCaloriesState.value) {
            is FoodAndCaloriesState.Error -> {
                FailedLoadingScreen(
                    errorMessage = "${(foodAndCaloriesState.value as FoodAndCaloriesState.Error).error}..",
                )
            }

            FoodAndCaloriesState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is FoodAndCaloriesState.SuccessWithData -> {
                Column(modifier = Modifier.padding(12.dp)) {
                    if (startDate.substring(0, 10) == endDate.value.substring(0, 10)) {
                        Text("Date: Today", style = MaterialTheme.typography.titleSmall)
                    } else {
                        Text(
                            "from Today to ${endDate.value.substring(0, 10)}",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    val foods =
                        (foodAndCaloriesState.value as FoodAndCaloriesState.SuccessWithData).foodAndCaloriesUIModel

                    if (foods.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(
                                    R.raw.no_food_data
                                )
                            )
                            val animationState = animateLottieCompositionAsState(
                                composition = composition,
                                iterations = LottieConstants.IterateForever
                            )

                            LottieAnimation(
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(200.dp),
                                composition = composition,
                                progress = { animationState.progress },
                            )
                            Text(
                                text = "There is no Data!",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    } else {
                        LazyColumn {
                            items(foods) { food ->
                                FoodItem(food, onDelete = deleteFood)
                            }
                        }
                    }
                }
            }

            else -> Unit
        }
    }

}

@Composable
fun FoodItem(food: FoodAndCaloriesUIModel, onDelete: (FoodAndCaloriesUIModel) -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = food.foodName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    onDelete(food)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Delete Food"
                    )
                }
            }
            Text("Amount: ${food.totalAmount}", style = MaterialTheme.typography.titleSmall)
            Text("Calories: ${food.calories}", style = MaterialTheme.typography.titleSmall)
            Text("Date: ${food.date.substring(0,16)}", style = MaterialTheme.typography.titleSmall)
        }
    }
}
