package com.example.fitnessapp.presentation.screens.profile_screen_package

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fitnessapp.theme.FitnessAppTheme

@Composable
fun AppPermissionsScreen() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Permissions Required for Fitness App Functionality:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        PermissionItem(
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            title = "Activity Recognition",
            description = "Detect physical activities like walking, running, and cycling.\nPermission: ACTIVITY_RECOGNITION"
        )

        PermissionItem(
            icon = Icons.Default.LocationOn,
            title = "Location Access",
            description = "Track your location during workouts like running or cycling.\nPermissions: ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION"
        )

        PermissionItem(
            icon = Icons.Default.Favorite,
            title = "Health Data Access",
            description = "Read and write health data using Google Fit or Health Connect.\n(No manifest permissions, user authorization is required)"
        )

        PermissionItem(
            icon = Icons.Default.Notifications,
            title = "Notifications",
            description = "Send workout reminders and motivational alerts.\nPermission: POST_NOTIFICATIONS (Android 13+)"
        )

        PermissionItem(
            icon = Icons.Default.Cloud,
            title = "Internet Access",
            description = "Access online workouts, sync data, and store backups.\nPermission: INTERNET"
        )

        PermissionItem(
            icon = Icons.Default.Storage,
            title = "Media & Storage Access",
            description = "Access profile images or export workout logs.\nPermissions: READ_EXTERNAL_STORAGE (deprecated), use Media APIs"
        )

        PermissionItem(
            icon = Icons.Default.DoNotDisturb,
            title = "Do Not Disturb Access",
            description = "Minimize distractions during workouts or meditation.\nPermission: ACCESS_NOTIFICATION_POLICY (optional)"
        )

        PermissionItem(
            icon = Icons.Default.Bluetooth,
            title = "Bluetooth Access",
            description = "Connect to fitness wearables or smart devices.\nPermissions: BLUETOOTH, BLUETOOTH_CONNECT (optional)"
        )
    }
}

@Composable
fun PermissionItem(icon: ImageVector, title: String, description: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column {
            Text(text = title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
            Text(text = description, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPermissionsScreenPreview() {
    FitnessAppTheme {
        AppPermissionsScreen()
    }
}
