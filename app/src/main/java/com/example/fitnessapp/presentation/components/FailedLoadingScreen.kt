package com.example.fitnessapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.fitnessapp.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.fitnessapp.theme.FitnessAppTheme

@Composable
fun FailedLoadingScreen(onFailed: () -> Unit = {}, errorMessage: String = "Unknown Error...") {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_internet_animation))

    val animationState = animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        LottieAnimation(
            composition = composition,
            progress = { animationState.progress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f)
        )

        Text(
            text = "Something Went Wrong..",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = errorMessage,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(vertical = 60.dp)
            ,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
            elevation = ButtonDefaults.buttonElevation(1.dp),
            shape = RoundedCornerShape(10),
            onClick = {
                onFailed()
            }
        ) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = "Retry",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )

        }
    }
}

@Preview
@Composable
private fun Prev() {
    FitnessAppTheme {
        FailedLoadingScreen()
    }
}
