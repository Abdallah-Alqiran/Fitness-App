package com.example.fitnessapp.data.datasources.remote

import com.example.fitnessapp.data.datasources.remote.model.FoodCaloriesModel
import com.example.fitnessapp.data.datasources.remote.model.Muscles
import com.example.fitnessapp.data.datasources.remote.model.UserInfoDataModel
import com.example.fitnessapp.presentation.screens.healthy_recipes_screen.model.RecipesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val userId: String?
        get() = auth.currentUser?.uid

    suspend fun createUserEmailAndPassword(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getAllMuscles(): List<Muscles> {
        return try {
            val cacheSnapshot = firestore.collection("Muscles")
                .orderBy("id")
                .get(Source.CACHE)
                .await()
            if (!cacheSnapshot.isEmpty) {
                return cacheSnapshot.map { it.toObject(Muscles::class.java) }
            }

            val snapshot = firestore.collection("Muscles")
                .orderBy("id")
                .get()
                .await()

            if (!snapshot.isEmpty) {
                snapshot.map { it.toObject(Muscles::class.java) }
            } else {
                throw NoSuchElementException("No muscle data found in cache or server.")
            }
        } catch (e: FirebaseFirestoreException) {
            throw e
        }
    }

    fun saveUserDataFirestore(
        userData: Map<String, Any>
    ) {
        if (userId == null) return
        try {
            firestore.collection("Users").document(userId!!)
                .set(userData, SetOptions.merge())
                .addOnFailureListener {
                    throw NoSuchElementException("Sorry we can't add the User")
                }
                .addOnSuccessListener {
                }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUserDataFires(): UserInfoDataModel? {
        return try {
            val snapshot = firestore.collection("Users").document(userId!!)
                .get()
                .await()
            snapshot.toObject(UserInfoDataModel::class.java)
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun getAllRecipes(): List<RecipesModel> {
        return try {
            val cacheSnapshot = firestore.collection("Recipes")
                .orderBy("id")
                .get(Source.CACHE)
                .await()
            if (!cacheSnapshot.isEmpty) {
                cacheSnapshot.map { it.toObject(RecipesModel::class.java) }
            }

            val snapshot = firestore.collection("Recipes")
                .orderBy("id")
                .get()
                .await()
            if (!snapshot.isEmpty) {
                snapshot.map { it.toObject(RecipesModel::class.java) }
            } else {
                throw NoSuchElementException("No Recipes data found in cache or server.")
            }
        } catch (e: FirebaseFirestoreException) {
            throw e
        }
    }

    suspend fun getAllFoodsWithCalories(): List<FoodCaloriesModel> {
        return try {
            val cacheSnapshot = firestore.collection("FoodsWithCalories")
                .orderBy("foodName")
                .get(Source.CACHE)
                .await()
            if (!cacheSnapshot.isEmpty) {
                cacheSnapshot.map { it.toObject(FoodCaloriesModel::class.java) }
            }

            val snapshot = firestore.collection("FoodsWithCalories")
                .orderBy("foodName")
                .get()
                .await()
            if (!snapshot.isEmpty) {
                snapshot.map { it.toObject(FoodCaloriesModel::class.java) }
            } else {
                throw NoSuchElementException("No Foods data found in cache or server.")
            }
        } catch (e: FirebaseFirestoreException) {
            throw e
        }
    }

}