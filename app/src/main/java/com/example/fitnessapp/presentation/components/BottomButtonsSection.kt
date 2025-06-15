package com.example.fitnessapp.presentation.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fitnessapp.theme.FitnessAppTheme

@Composable
fun BottomButtonsSection(
    modifier: Modifier = Modifier,
    onContinueClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    backEnabled: Boolean = true,
    errorMessage: String = "",
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall,
        )

        DefaultButton(
            onClick = onContinueClick,
        )

        DefaultButton(
            text = "Back",
            onClick = onBackClick,
            enabled = backEnabled,
            border = MaterialTheme.colorScheme.primary,
            buttonColor = ButtonDefaults.buttonColors(containerColor =MaterialTheme.colorScheme.surface),
            modifier = modifier
        )
    }
}


@Preview
@Composable
private fun Prev() {
    FitnessAppTheme {
        BottomButtonsSection()
    }
}