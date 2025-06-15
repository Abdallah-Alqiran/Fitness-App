package com.example.fitnessapp.presentation.screens.auth.user_data_package.set_goals_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnessapp.R
import com.example.fitnessapp.presentation.components.BottomButtonsSection
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.viewModels.save_userData_viewModel.SaveUserDataState
import com.example.fitnessapp.presentation.viewModels.save_userData_viewModel.SaveUserDataViewModel

@Composable
fun SetGoalsScreen(
    onSetGoals: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val personGoals = remember { mutableStateOf("") }
    val isGoalSelected = remember { mutableStateOf("") }
    var loadTrigger by remember { mutableStateOf(false) }

    val saveUserDataViewModel = hiltViewModel<SaveUserDataViewModel>()
    val userDataState = saveUserDataViewModel.userDataState.collectAsStateWithLifecycle()

    if (loadTrigger) {
        LaunchedEffect(Unit) {
            saveUserDataViewModel.saveDataToFirestore(
                mapOf("goal" to personGoals.value)
            )
            loadTrigger = false
        }
    }

    when (userDataState.value) {
        is SaveUserDataState.Error -> FailedLoadingScreen()
        SaveUserDataState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
            return
        }
        SaveUserDataState.Success -> {
            LaunchedEffect(Unit) {
                onSetGoals()
                saveUserDataViewModel.resetUserDataState()
            }
        }
        else -> Unit
    }

    val items = listOf(
        R.drawable.loseweight,
        R.drawable.weightscale,
        R.drawable.machine
    )
    val goals = listOf("Lose Weight", "Gain Weight", "Maintain Weight")
    val selectedIndex = remember { mutableIntStateOf(-1) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val maxHeight = maxHeight
        val maxWidth = maxWidth
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = maxWidth * 0.05f)
        ) {

            Spacer(modifier = Modifier.height(maxHeight * 0.05f))
            Text(
                text = "Set Your Goals",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = maxHeight * 0.02f)
            )


            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                goals.forEachIndexed { index, goalText ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(bottom = maxHeight * 0.02f)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .height(maxHeight * 0.15f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = 2.dp,
                                    color = if (selectedIndex.intValue == index)
                                        MaterialTheme.colorScheme.primary
                                    else Color.Gray,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    selectedIndex.intValue = index
                                    personGoals.value = goals[index]
                                    isGoalSelected.value = ""
                                }
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            Image(
                                painter = painterResource(id = items[index]),
                                contentDescription = "Goal Image",
                                modifier = Modifier
                                    .padding(maxWidth * 0.04f)
                                    .fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Text(
                            text = goalText,
                            modifier = Modifier.padding(top = maxHeight * 0.01f),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }


                BottomButtonsSection(
                    onContinueClick = {
                        if (personGoals.value.isEmpty()) {
                            isGoalSelected.value = "Please select your goals"
                        } else {
                            loadTrigger = true
                        }
                    },
                    onBackClick = onBack,
                    errorMessage = isGoalSelected.value
                )



        }
    }
}
