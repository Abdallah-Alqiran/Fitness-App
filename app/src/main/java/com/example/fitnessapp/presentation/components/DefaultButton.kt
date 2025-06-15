package com.example.fitnessapp.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fitnessapp.theme.FitnessAppTheme

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String = "Continue",
    enabled: Boolean = true,
    border: Color = colorScheme.surface,
    buttonColor: ButtonColors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
) {

    Button(
        modifier = modifier
            .width(220.dp),
        border = BorderStroke(2.dp, border),
        colors = buttonColor,
        onClick = {
            if (enabled)
                onClick()
        },
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.displaySmall,
            color = colorScheme.onBackground,
        )
    }


}


@Preview
@Composable
private fun Hello() {
    FitnessAppTheme {
        DefaultButton()
    }
}