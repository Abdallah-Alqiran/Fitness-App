package com.example.fitnessapp.presentation.screens.dashboared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitnessapp.R

@Composable
fun DiscoverSection(
    onRecipes: () -> Unit,
    onWorkouts: () -> Unit,
    onFoodHistory: () -> Unit,
    onSearch: () -> Unit,
    onHealth: () -> Unit,
    onProfile: () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Discover",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            DiscoverButton(
                icon = painterResource(id = R.drawable.add_food),
                label = "Add Foods",
                onClick = { onSearch() },
                modifier = Modifier.weight(1f)
            )
            DiscoverButton(
                icon = painterResource(id = R.drawable.dinner_icon),
                label = "Recipes",
                onClick = {
                    onRecipes()
                },
                modifier = Modifier.weight(1f)
            )
            DiscoverButton(
                icon = painterResource(id = R.drawable.food_package),
                label = "Food History",
                onClick = {
                    onFoodHistory()
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            DiscoverButton(
                icon = painterResource(id = R.drawable.baseline_fitness_center_24),
                label = "Workouts",
                onClick = {
                    onWorkouts()
                },
                modifier = Modifier.weight(1f)
            )
            DiscoverButton(
                icon = painterResource(id = R.drawable.heart_pulse_solid),
                label = "Health",
                onClick = { onHealth() },
                modifier = Modifier.weight(1f)
            )
            DiscoverButton(
                icon = painterResource(id = R.drawable.ic_face),
                label = "Profile",
                onClick = { onProfile() },
                modifier = Modifier.weight(1f)
            )
        }
    }
}