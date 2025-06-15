package com.example.fitnessapp.di

import android.content.Context
import androidx.room.Room
import com.example.fitnessapp.constants.Constants.Companion.FOOD_AND_CALORIES_DATABASE
import com.example.fitnessapp.data.datasources.local.FoodAndCaloriesDao
import com.example.fitnessapp.data.datasources.local.FoodAndCaloriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FoodAndCaloriesDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = FoodAndCaloriesDatabase::class.java,
            name = FOOD_AND_CALORIES_DATABASE
        ).fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideFoodAndCaloriesDao(foodAndCaloriesDatabase: FoodAndCaloriesDatabase): FoodAndCaloriesDao {
        return foodAndCaloriesDatabase.foodAndCalorieDao()
    }

}