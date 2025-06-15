package com.example.fitnessapp.presentation.screens.healthy_recipes_screen.model

data class RecipesModel(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val protein: Int = 0,
    val calories: Int = 0,
    val preparationTime: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val steps: String = ""
)

data class Ingredient (
    val name: String = "",
    val quantity: String = ""
)