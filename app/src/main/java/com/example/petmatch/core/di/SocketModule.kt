package com.example.petmatch.core.di

import com.example.petmatch.core.socket.PetMatchSocketManager
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import com.example.petmatch.features.health.data.datasources.local.dao.HealthDao // Nuevo import
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {

    @Provides
    @Singleton
    fun provideSocketManager(
        petMatchDao: PetMatchDao,
        healthDao: HealthDao // Inyectamos el nuevo DAO
    ): PetMatchSocketManager {
        return PetMatchSocketManager(petMatchDao, healthDao)
    }
}