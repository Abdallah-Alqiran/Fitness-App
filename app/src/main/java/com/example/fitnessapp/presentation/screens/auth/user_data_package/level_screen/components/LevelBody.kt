package com.example.fitnessapp.presentation.screens.auth.user_data_package.level_screen.components


import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnessapp.R
import com.example.fitnessapp.presentation.components.BottomButtonsSection
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.screens.auth.user_data_package.level_screen.models.LevelList
import com.example.fitnessapp.presentation.viewModels.save_userData_viewModel.SaveUserDataState
import com.example.fitnessapp.presentation.viewModels.save_userData_viewModel.SaveUserDataViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LevelContent(
    onPersonLevel: () -> Unit,
    levelList: MutableList<LevelList>,
    onBack: () -> Unit = {}
) {
    val personLevel = remember { mutableStateOf("") }
    val isLevelSelected = remember { mutableStateOf("") }
    var loadTrigger by remember { mutableStateOf(false) }

    val saveUserDataViewModel = hiltViewModel<SaveUserDataViewModel>()
    val userDataState = saveUserDataViewModel.userDataState.collectAsStateWithLifecycle()

    if (loadTrigger) {
        LaunchedEffect(Unit) {
            saveUserDataViewModel.saveDataToFirestore(
                mapOf("level" to personLevel.value)
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
        }

        SaveUserDataState.Success -> {
            LaunchedEffect(Unit) {
                onPersonLevel()
                saveUserDataViewModel.resetUserDataState()
            }
        }

        else -> Unit
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        val maxHeight = maxHeight
        val maxWidth = maxWidth

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = maxWidth * 0.05f)
        ) {

            Spacer(modifier = Modifier.height(maxHeight * 0.05f))
            Text(
                text = stringResource(id = R.string.physical_activity_level),
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = maxHeight * 0.02f)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var oldSelected: MutableState<Boolean>? = null

                items(levelList.size) { i ->
                    val isSelected = remember { mutableStateOf(levelList[i].isSelected) }

                    val border = if (isSelected.value) {
                        BorderStroke(3.dp, colorScheme.primary)
                    } else BorderStroke(3.dp, colorScheme.onBackground)

                    RowElements(
                        levelList[i],
                        modifier = Modifier.clickable {
                            if (oldSelected?.value != null) {
                                oldSelected?.value = false
                            }
                            oldSelected = isSelected
                            isSelected.value = true

                            personLevel.value = levelList[i].levelName
                            isLevelSelected.value = ""
                        },
                        border = border
                    )
                }
            }


            BottomButtonsSection(
                errorMessage = isLevelSelected.value,
                onContinueClick = {
                    if (personLevel.value.isEmpty()) {
                        isLevelSelected.value = "Please select your level"
                    } else {
                        loadTrigger = true
                    }
                },
                onBackClick = onBack,
            )
        }
    }
}


@Composable
fun RowElements(
    levelList: LevelList,
    modifier: Modifier = Modifier,
    border: BorderStroke = BorderStroke(3.dp, colorScheme.onBackground)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Image(
            modifier = Modifier
                .size(64.dp)
                .padding(end = 12.dp),
            painter = painterResource(id = levelList.levelImage),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colorScheme.primary),
        )

        CardElement(
            levelList = levelList.levelName,
            modifier = modifier.weight(1f),
            border = border
        )
    }
}



@Composable
fun CardElement(levelList: String, modifier: Modifier = Modifier, border: BorderStroke) {
    Card(
        colors = CardDefaults.cardColors(colorScheme.surface),
        modifier = Modifier
            .height(56.dp),
        border = border,
        shape = CircleShape,
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = levelList,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
