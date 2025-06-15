package com.example.fitnessapp.presentation.screens.muscle_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.fitnessapp.R
import com.example.fitnessapp.data.datasources.remote.model.Exercises
import com.example.fitnessapp.data.datasources.remote.model.Muscles
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.screens.muscle_screen.viewModel.ExercisesViewModel
import com.example.fitnessapp.presentation.screens.muscle_screen.viewModel.MuscleState
import com.example.fitnessapp.theme.FitnessAppTheme


@Composable
fun ExerciseDetailScreen(id: Int) {

    val muscleViewModel = hiltViewModel<ExercisesViewModel>()
    val musclesState by muscleViewModel.muscleState.collectAsStateWithLifecycle()

    when (musclesState) {
        is MuscleState.Error -> {
            FailedLoadingScreen(
                errorMessage = "${(musclesState as MuscleState.Error).message}..",
                onFailed = {
                    muscleViewModel.loadMuscles()
                }
            )
        }

        is MuscleState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is MuscleState.Success -> {
            val musclesData = (musclesState as MuscleState.Success).muscles[id]
            ExerciseDetails(musclesData)
        }

    }
}

@Composable
fun ExerciseDetails(
    musclesData: Muscles
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(musclesData.exercises) { index, exercise ->
                ExerciseDetailItem(
                    name = exercise.name,
                    description = exercise.description,
                    sets = exercise.sets,
                    reps = exercise.reps,
                    image1 = exercise.image1,
                    image2 = exercise.image2,
                    isFirstItem = index == 0
                )
            }
        }
    }
}

@Composable
fun ExerciseDetailItem(
    name: String,
    description: String,
    sets: String,
    reps: String,
    image1: String,
    image2: String,
    isFirstItem: Boolean
) {
    if (!isFirstItem) {
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.surface)
    }

    Row(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 7.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(vertical = 7.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier
                    .padding(vertical = 1.dp)
            )

            Text(
                text = sets,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(vertical = 5.dp)
            )

            Text(
                text = reps,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(vertical = 5.dp)
            )

        }
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedImage(image1, image2, imageSize = 160.dp)
        }
    }
}

@Composable
fun AnimatedImage(image1: String, image2: String, imageSize: Dp) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentImage by remember { mutableStateOf(image1) }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(700)
            currentImage = if (currentImage == image1) image2 else image1
        }
    }

    Box(
        modifier = Modifier
            .size(imageSize)
            .clickable { isPlaying = !isPlaying },
        contentAlignment = Alignment.Center
    ) {

        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(image1)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { isPlaying = !isPlaying }
                    .alpha(if (currentImage == image1) 1f else 0f),
                placeholder = painterResource(id = R.drawable.ex_exercise),
                error = painterResource(id = R.drawable.baseline_notifications_24)
            )

            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(image2)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .alpha(if (currentImage == image2) 1f else 0f),
                placeholder = painterResource(id = R.drawable.ex_exercise),
                error = painterResource(id = R.drawable.baseline_notifications_24)
            )
        }

        Icon(
            painter = painterResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
            contentDescription = "Play/Pause",
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.BottomStart)
                .padding(5.dp, 0.dp, 0.dp, 0.dp)
        )
    }
}


@Preview
@Composable
private fun Prev() {
    FitnessAppTheme {
        ExerciseDetails(
            musclesData = Muscles(
                id = 10,
                muscle = "Back",
                exercises = listOf(
                    Exercises(
                        name = "Single Arm Row",
                        sets = "4 sets",
                        reps = "10 reps",
                        description = "Builds lets and rhomboids",
                        image1 = "",
                        image2 = ""
                    ),
                    Exercises(
                        name = "Single Arm Row",
                        sets = "4 sets",
                        reps = "10 reps",
                        description = "Builds lets and rhomboids",
                        image1 = "",
                        image2 = ""
                    ),

                )
            )
        )
    }
}