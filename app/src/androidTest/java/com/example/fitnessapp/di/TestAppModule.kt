package com.example.fitnessapp.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import com.example.fitnessapp.data.datasources.local.FoodAndCaloriesDatabase
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, FoodAndCaloriesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}