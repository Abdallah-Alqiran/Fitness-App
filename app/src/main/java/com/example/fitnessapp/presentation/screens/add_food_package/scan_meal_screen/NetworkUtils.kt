package com.example.fitnessapp.presentation.screens.add_food_package.scan_meal_screen

import com.example.fitnessapp.presentation.screens.add_food_package.scan_meal_screen.model.FoodResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

suspend fun detectFoodFromServer(base64Image: String): FoodResponse? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val mediaType = "application/json".toMediaType()
            val jsonBody = """{"image": "$base64Image"}"""
            val body = jsonBody.toRequestBody(mediaType)

            val request = Request.Builder()
                .url("https://meal-calories-calculating.vercel.app/api/detect_food")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                responseBody?.let {
                    return@withContext Json.decodeFromString<FoodResponse>(it)
                }
            } else {
                println("Server Error: ${response.code}")
            }
            null
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            null
        }
    }
}