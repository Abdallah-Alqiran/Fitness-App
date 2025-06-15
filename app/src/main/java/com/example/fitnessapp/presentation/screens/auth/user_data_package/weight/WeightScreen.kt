package com.example.fitnessapp.presentation.screens.auth.user_data_package.weight

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnessapp.presentation.components.FailedLoadingScreen
import com.example.fitnessapp.presentation.viewModels.save_userData_viewModel.SaveUserDataState
import com.example.fitnessapp.presentation.viewModels.save_userData_viewModel.SaveUserDataViewModel
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin
import com.example.fitnessapp.presentation.components.BottomButtonsSection

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun WeightScreen(
    onWeight: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var weight by remember { mutableFloatStateOf(70f) }
    var loadTrigger by remember { mutableStateOf(false) }

    val saveUserDataViewModel = hiltViewModel<SaveUserDataViewModel>()
    val userDataState = saveUserDataViewModel.userDataState.collectAsStateWithLifecycle()

    if (loadTrigger) {
        LaunchedEffect(Unit) {
            saveUserDataViewModel.saveDataToFirestore(mapOf("weight" to weight.toInt()))
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
                onWeight()
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
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = maxWidth * 0.05f)
        ) {
            Spacer(modifier = Modifier.height(maxHeight * 0.05f))
            Text(
                text = "What is your weight?",
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = maxHeight * 0.02f)
                ) {
                    Text(
                        text = "${weight.toInt()}",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(end = maxWidth * 0.02f)
                    )
                    Text(
                        text = "Kg",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                        .padding(vertical = maxHeight * 0.02f)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircularDial(10, 200)
                        drawPointer(weight, 10, 200)
                    }
                }

                Slider(
                    value = weight,
                    onValueChange = { weight = it },
                    valueRange = 10f..200f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            BottomButtonsSection(
                onContinueClick = { loadTrigger = true },
                onBackClick = onBack
            )

        }
    }
}







fun DrawScope.drawCircularDial(minWeight: Int, maxWeight: Int) {
        drawCircle(
            color = Color(0xFF29E33C),
            style = Stroke(width = 60f)
        )

        val radius = size.minDimension / 2
        val totalSteps = maxWeight - minWeight + 10
        val stepAngle = 360f / totalSteps

        for (i in 0..<totalSteps step 10) {
            val weightValue = minWeight + i
            val angle = i * stepAngle - 90f
            val x = (radius * cos(toRadians(angle.toDouble()))).toFloat() + center.x
            val y = (radius * sin(toRadians(angle.toDouble()))).toFloat() + center.y

            drawContext.canvas.nativeCanvas.drawText(
                weightValue.toString(),
                x,
                y,
                Paint().apply {
                    textSize = 20f
                    textAlign = Paint.Align.CENTER
                }
            )
        }
    }

    fun DrawScope.drawPointer(weight: Float, minWeight: Int, maxWeight: Int) {
        val totalSteps = maxWeight - minWeight + 10
        val stepAngle = 360f / totalSteps
        val angle = (weight - minWeight) * stepAngle - 90f

        val radius = size.minDimension / 2
        val pointerLength = radius * 0.6f
        val x = (pointerLength * cos(toRadians(angle.toDouble()))).toFloat() + center.x
        val y = (pointerLength * sin(toRadians(angle.toDouble()))).toFloat() + center.y

        drawLine(
            color = Color.Green,
            start = center,
            end = Offset(x, y),
            strokeWidth = 10f
        )
    }
