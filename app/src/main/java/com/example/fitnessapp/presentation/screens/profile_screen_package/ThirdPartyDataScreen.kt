package com.example.fitnessapp.presentation.screens.profile_screen_package

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.fitnessapp.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Composable
fun HealthPermissionStatusScreen() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("health_prefs", Context.MODE_PRIVATE)
    }

    var isHealthGranted by remember { mutableStateOf(prefs.getBoolean("isHealthGranted", false)) }
    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        flow {
            while (true) {
                emit(prefs.getBoolean("isHealthGranted", false))
                kotlinx.coroutines.delay(1000)
            }
        }.collectLatest { granted ->
            isHealthGranted = granted
            isLoaded = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_health_connect),
                    contentDescription = "Health Connect Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(80.dp)
                )
            }
        }

        Text(
            text = """
                This app requests access to your health data through Health Connect. 
                
                The data we access includes (Steps/Exercise/Sleep/Distance/General Health Data/Background Health Data Access)
                
                We use this information to provide accurate fitness insights and personalized health tracking.
            """.trimIndent(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (isLoaded) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isHealthGranted) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (isHealthGranted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = if (isHealthGranted) {
                        "Health Connect permission is granted."
                    } else {
                        "Health Connect permission is not granted."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
        }
    }
}