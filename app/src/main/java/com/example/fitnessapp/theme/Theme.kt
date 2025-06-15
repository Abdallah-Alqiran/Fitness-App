package com.example.fitnessapp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    // for buttons
    primary = Color(0xFF29E33C),
    onPrimary = Color(0xFFFFFFFF),
    // for screens background
    background = Color.Black,
    onBackground = Color.White,
    // for bar color
    surface = DarkGreySurface,
    onSurface = Color.White,
    secondary = Color.Gray,
    onSecondary = LightGreySurface,
    error = Color.Red,

    )
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2FC94C),
    onPrimary = Color.White,

    background = Color(0xFFFDFDFD),
    onBackground = Color.Black,

    surface = Color(0xFFF0F0F0),
    onSurface = Color.Black,

    secondary = Color(0xFF9E9E9E),
    onSecondary = Color(0xFF2C2C2C),

    error = Color(0xFFD32F2F)
)

@Composable
fun FitnessAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

//    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}