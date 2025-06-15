package com.example.fitnessapp.presentation.screens.add_food_package.search_food_and_add_screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnessapp.data.datasources.remote.model.FoodCaloriesModel
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.screens.add_food_package.search_food_and_add_screens.viewModel.FoodSearchState
import com.example.fitnessapp.presentation.screens.add_food_package.search_food_and_add_screens.viewModel.FoodSearchViewModel


@Composable
fun SearchFoodScreen(onSearchFood:(foodName: String, calories: String) -> Unit) {

    val foodViewModel = hiltViewModel<FoodSearchViewModel>()
    val foodState by foodViewModel.foodState.collectAsStateWithLifecycle()

    when (foodState) {
        is FoodSearchState.Error -> {
            FailedLoadingScreen(
                errorMessage = "${(foodState as FoodSearchState.Error).message}...",
                onFailed = {
                    foodViewModel.loadFood()
                }
            )
        }

        is FoodSearchState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is FoodSearchState.Success -> {
            val textState = remember { mutableStateOf(TextFieldValue("")) }
            Column {
                AddFoodSelectOption(textState)
                FoodList(onSearchFood, state = textState, foodCaloriesList = (foodState as FoodSearchState.Success).foods)
            }
        }
    }


}

@Composable
fun AddFoodSelectOption(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        placeholder = { Text("Search Food", style = TextStyle(color = MaterialTheme.colorScheme.onBackground), fontSize = 17.sp) },
        onValueChange = { value -> state.value = value },
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "") },
        trailingIcon = {
            if (state.value.text.isNotEmpty()) {
                IconButton(onClick = { state.value = TextFieldValue("") }) {
                    Icon(Icons.Default.Close, contentDescription = "")
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        singleLine = true,
    )
}


@Composable
fun FoodList(onSearchFood:(foodName: String, calories: String) -> Unit, state: MutableState<TextFieldValue>, foodCaloriesList: List<FoodCaloriesModel>) {
    val searchedText = state.value.text
    val filteredFoods = if (searchedText.isEmpty()) {
        foodCaloriesList
    } else {
        foodCaloriesList.filter { food ->
            food.foodName.lowercase().contains(searchedText.lowercase())
        }
    }

    Column {
        Text(
            text = "All values are per 100gm",
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 8.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )

        LazyColumn {
            items(filteredFoods) { (foodName, calories) ->
                Row(
                    modifier = Modifier
                        .clickable {
                            onSearchFood(foodName, calories)
                        }
                        .padding(14.dp)
                        .fillMaxSize(),
                    ) {
                    Column {
                        Text(
                            text = foodName,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "Calories: $calories kcal",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }


    }
}
