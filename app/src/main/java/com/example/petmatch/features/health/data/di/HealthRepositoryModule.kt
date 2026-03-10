package com.example.petmatch.features.health.data.di

import com.example.petmatch.features.health.data.repositories.HealthRepositoryImpl
import com.example.petmatch.features.health.domain.repositories.HealthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HealthRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHealthRepository(
        healthRepositoryImpl: HealthRepositoryImpl
    ): HealthRepository
}