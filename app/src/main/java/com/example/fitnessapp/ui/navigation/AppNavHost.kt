package com.example.fitnessapp.ui.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnessapp.ui.screens.food_screen.FoodScreen
import com.example.fitnessapp.ui.screens.gender_screen.GenderScreen
import com.example.fitnessapp.ui.screens.height_select.NumberPickerDemo
import com.example.fitnessapp.ui.screens.level_screen.PhysicalActivityLevel
import com.example.fitnessapp.ui.screens.signup_screen.SignUpScreen
import com.example.fitnessapp.ui.screens.splash_screen.SplashScreen

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen {
                navController.navigate("signup") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        composable("signup") {
            SignUpScreen(
                onSignUp = { username, email, password ->
                    navController.navigate("gender") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                goToLogin = {
                    navController.navigate("login") {
                        popUpTo("signup")
                    }
                }
            )
        }

        composable("level") {
            PhysicalActivityLevel(
                onPersonLevel = { personLevel ->
                    navController.navigate("food") {
                        popUpTo("level") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("gender") {
            GenderScreen {gender ->
                navController.navigate("height")
            }
        }

        composable("food") {
            FoodScreen()
        }

        composable("login") {
            LoginScreen(
                onLogin = {
                    navController.navigate("food") {
                        popUpTo(0)
                    }
                },
                goToSignUp = {
                    navController.navigate("signup")
                }
            )
        }

        composable("height") {
            NumberPickerDemo(
                onHeight = {
                    navController.navigate("level")
                }
            )
        }


    }
}


