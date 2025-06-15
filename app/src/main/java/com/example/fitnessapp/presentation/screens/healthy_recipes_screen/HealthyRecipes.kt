package com.example.fitnessapp.presentation.screens.healthy_recipes_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.fitnessapp.R
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.screens.healthy_recipes_screen.model.RecipesModel
import com.example.fitnessapp.presentation.screens.healthy_recipes_screen.viewModel.HealthyRecipesViewModel
import com.example.fitnessapp.presentation.screens.healthy_recipes_screen.viewModel.RecipesState


@Composable
fun RecipesScreen(onClick: (Int) -> Unit) {


    val healthyRecipesViewModel = hiltViewModel<HealthyRecipesViewModel>()
    val healthyRecipesState by healthyRecipesViewModel.healthyRecipesState.collectAsStateWithLifecycle()

    when (healthyRecipesState) {
        is RecipesState.Error -> {
            FailedLoadingScreen(
                errorMessage = "${(healthyRecipesState as RecipesState.Error).message}...",
                onFailed = {
                    healthyRecipesViewModel.loadRecipes()
                }
            )
        }

        is RecipesState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is RecipesState.Success -> {
            RecipesItems((healthyRecipesState as RecipesState.Success).recipes, onClick)
        }
    }

}

@Composable
fun RecipesItems(recipes: List<RecipesModel>, onClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        items(recipes) { recipe ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(185.dp),
                shape = RoundedCornerShape(16.dp),
                onClick = { onClick(recipe.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface),
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(recipe.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = recipe.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth(),
                        placeholder = painterResource(id = R.drawable.dinner_icon),
                        error = painterResource(id = R.drawable.baseline_notifications_24)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        "Protein: ${recipe.protein}g",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(start = 6.dp, end = 6.dp)
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}