package com.example.fitnessapp.di

import com.example.fitnessapp.data.datasources.remote.repository.RemoteRepoImpl
import com.example.fitnessapp.domain.repo.FirebaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMusclesRepository(impl: RemoteRepoImpl): FirebaseRepository
}