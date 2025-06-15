package com.example.fitnessapp.presentation.screens.waterScreen

import android.content.Context
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.R
import com.example.fitnessapp.presentation.components.DefaultButton
import java.util.Locale

@Composable
fun WaterTrackerScreen() {

    var showDialog by remember { mutableStateOf(false) }
    var customAmount by remember { mutableStateOf("") }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("WaterReminderPrefs", Context.MODE_PRIVATE)

    var startHour by remember { mutableIntStateOf(sharedPreferences.getInt("start_hour", 8)) }
    var startMinute by remember { mutableIntStateOf(sharedPreferences.getInt("start_minute", 0)) }
    var endHour by remember { mutableIntStateOf(sharedPreferences.getInt("end_hour", 22)) }
    var endMinute by remember { mutableIntStateOf(sharedPreferences.getInt("end_minute", 0)) }
    var interval by remember { mutableIntStateOf(sharedPreferences.getInt("interval_hours", 2)) }
    var totalVolume by remember { mutableIntStateOf(sharedPreferences.getInt("total_volume", 0)) }

    var isFocused by remember { mutableStateOf(false) }

    val savedGoal = sharedPreferences.getInt("daily_goal", 2000)
    var dailyGoal by remember { mutableIntStateOf(savedGoal) }

    val waterOptions = listOf(
        Pair(200, R.drawable.small_bottle),
        Pair(500, R.drawable.medium_bottle),
        Pair(1000, R.drawable.large_bottle)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Water Volume
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Water Today's Volume:",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(15.dp))

            Card(
                modifier = Modifier
                    .width(170.dp)
                    .height(90.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "$totalVolume",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = " ml",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        IconButton(onClick = {
                            totalVolume = 0
                            sharedPreferences.edit().putInt("total_volume", totalVolume).apply()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Water Amount Buttons
        Row(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth()
                .height(90.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            waterOptions.forEach { (amount, imageRes) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clickable { totalVolume += amount }
                    )
                    Text(
                        text = "$amount ml",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp
                    )
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.custom_bottle),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clickable { showDialog = true }
                )
                Text(
                    text = "Custom",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        DefaultButton(
            text = "Save Water",
            onClick = {
                sharedPreferences.edit()
                    .putInt("total_volume", totalVolume)
                    .apply()
                Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Set Daily Water Habit",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Daily Goal:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = dailyGoal.toString(),
                onValueChange = {
                    dailyGoal = it.toIntOrNull() ?: dailyGoal
                    sharedPreferences.edit().putInt("daily_goal", dailyGoal).apply()
                },
                label = {
                    Text(
                        "Daily Goal (ml)",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier
                    .onFocusChanged { isFocused = it.isFocused },
                shape = RoundedCornerShape(16.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start Time Picker
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Start Time:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { showStartTimePicker = true },
                modifier = Modifier.width(150.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = String.format(Locale.US, "%02d:%02d", startHour, startMinute),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // End Time Picker
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "End Time:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { showEndTimePicker = true },
                modifier = Modifier.width(150.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = String.format(Locale.US, "%02d:%02d", endHour, endMinute),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interval Slider
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Every ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$interval ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "hour notification:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Slider(
                value = interval.toFloat(),
                onValueChange = { interval = it.toInt() },
                valueRange = 1f..9f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

        }
        Spacer(modifier = Modifier.height(16.dp))


        DefaultButton(
            text = "Save Settings",
            onClick = {
                sharedPreferences.edit()
                    .putInt("start_hour", startHour)
                    .putInt("start_minute", startMinute)
                    .putInt("interval_hours", interval)
                    .putInt("total_volume", totalVolume)
                    .apply()

                ReminderScheduler.scheduleWaterReminders(context)

                Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
            }
        )

    }

    // Custom Amount Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Enter Custom Amount", color = MaterialTheme.colorScheme.primary) },
            text = {
                OutlinedTextField(
                    value = customAmount,
                    onValueChange = { customAmount = it.filter { char -> char.isDigit() } },
                    label = { Text("Amount in ml", style = MaterialTheme.typography.labelMedium) },
                    textStyle = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = customAmount.toIntOrNull()
                        if (amount != null && amount > 0) {
                            totalVolume += amount
                            showDialog = false
                            customAmount = ""
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Time Pickers
    if (showStartTimePicker) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                startHour = hour
                startMinute = minute
                showStartTimePicker = false
            },
            startHour,
            startMinute,
            true
        ).show()
    }

    if (showEndTimePicker) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                endHour = hour
                endMinute = minute
                showEndTimePicker = false
            },
            endHour,
            endMinute,
            true
        ).show()
    }
}
