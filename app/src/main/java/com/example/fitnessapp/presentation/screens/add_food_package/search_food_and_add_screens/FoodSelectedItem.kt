package com.example.fitnessapp.presentation.screens.add_food_package.search_food_and_add_screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnessapp.data.datasources.local.FoodAndCaloriesLocalModel
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.components.SuccessDialog
import com.example.fitnessapp.presentation.viewModels.foodAndCalories_viewModel.FoodAndCaloriesState
import com.example.fitnessapp.presentation.viewModels.foodAndCalories_viewModel.FoodAndCaloriesViewModel


@Composable
fun FoodSelectedItem(foodName: String, calories: String) {
    var quantity by remember { mutableIntStateOf(1) }
    val totalCalories: Double = calories.toInt() * quantity.toDouble()
    val context = LocalContext.current

    val foodAndCalorieViewModel = hiltViewModel<FoodAndCaloriesViewModel>()
    val foodAndCaloriesState =
        foodAndCalorieViewModel.foodAndCaloriesState.collectAsStateWithLifecycle()
    var foodInsert by remember { mutableStateOf<FoodAndCaloriesLocalModel?>(null) }
    var loadTrigger by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(true) }

    if (loadTrigger) {
        LaunchedEffect(foodInsert) {
            foodAndCalorieViewModel.insertFoodAndCalories(foodInsert!!)
            loadTrigger = false
            showDialog = true
        }
    }

    when (foodAndCaloriesState.value) {
        is FoodAndCaloriesState.Error -> {
            FailedLoadingScreen(onFailed = {
                foodAndCalorieViewModel.insertFoodAndCalories(foodInsert!!)
            }, errorMessage = "Failed Saving to database")
        }
        FoodAndCaloriesState.Loading -> CircularProgressIndicator()
        FoodAndCaloriesState.Success -> {
            if (showDialog) {
                SuccessDialog(onDismiss = {
                    showDialog = false
                    foodAndCalorieViewModel.resetState()
                })
            }
        }
        else -> Unit
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),

            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = foodName,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Calories per unit: $calories cal",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { if (quantity > 1) quantity-- },
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("-", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "$quantity",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { quantity++ },
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("+")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Total Calories: $totalCalories cal",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        foodInsert = FoodAndCaloriesLocalModel(
                            foodName = foodName,
                            calories = totalCalories,
                            totalAmount = quantity
                        )
                        Toast.makeText(
                            context,
                            "$totalCalories cal have been added. $foodName $quantity",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadTrigger = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Add to My Diary")
                }
            }
        }
    }
}