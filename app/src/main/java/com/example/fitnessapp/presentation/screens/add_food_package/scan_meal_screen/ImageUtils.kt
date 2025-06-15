package com.example.fitnessapp.presentation.screens.add_food_package.scan_meal_screen

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    val base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
    return "data:image/jpeg;base64,$base64"
}