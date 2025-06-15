package com.example.fitnessapp.presentation.screens.auth.user_data_package.gender_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnessapp.R
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.viewModels.save_userData_viewModel.SaveUserDataState
import com.example.fitnessapp.presentation.viewModels.save_userData_viewModel.SaveUserDataViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.draw.alpha
import com.example.fitnessapp.presentation.components.BottomButtonsSection


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GenderScreen(onGender: () -> Unit = {}) {
    var showError by remember { mutableStateOf(false) }
    var loadTrigger by remember { mutableStateOf(false) }

    val items = listOf(R.drawable.male, R.drawable.female)
    val gender = listOf("Male", "Female")
    val selectedIndex = remember { mutableIntStateOf(-1) }
    val errorMessage = remember { mutableStateOf("") }

    val saveUserDataViewModel = hiltViewModel<SaveUserDataViewModel>()
    val userDataState = saveUserDataViewModel.userDataState.collectAsStateWithLifecycle()

    if (loadTrigger) {
        LaunchedEffect(Unit) {
            saveUserDataViewModel.saveDataToFirestore(
                mapOf("gender" to gender[selectedIndex.intValue])
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
                onGender()
                saveUserDataViewModel.resetUserDataState()
            }
        }
        else -> Unit
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val maxHeight = maxHeight
        val maxWidth = maxWidth
        val imageSize = maxWidth * 0.4f
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = maxWidth * 0.05f)
        ) {
            Spacer(modifier = Modifier.height(maxHeight * 0.05f))

            Text(
                text = "What is Your Gender?",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = maxHeight * 0.02f)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    gender.forEachIndexed { index, label ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(bottom = maxHeight * 0.02f)
                                .fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(imageSize)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                        width = 2.dp,
                                        color = if (selectedIndex.intValue == index)
                                            MaterialTheme.colorScheme.primary else Color.Gray,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        selectedIndex.intValue = index
                                        showError = false
                                    }
                                    .background(color = MaterialTheme.colorScheme.surface)
                            ) {
                                Image(
                                    painter = painterResource(id = items[index]),
                                    contentDescription = "Gender Image",
                                    modifier = Modifier
                                        .padding(imageSize * 0.1f)
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Text(
                                modifier = Modifier.padding(top = maxHeight * 0.01f),
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    if (showError) {
                        errorMessage.value =  "Please select a gender"
                    }
                }
            }

            BottomButtonsSection(
                errorMessage = errorMessage.value,
                onContinueClick = {
                    if (selectedIndex.intValue == -1) {
                        showError = true
                    } else {
                        loadTrigger = true
                    }
                },
                onBackClick = { },
                modifier = Modifier.alpha(0f),
                backEnabled = false
            )
        }
    }
}
