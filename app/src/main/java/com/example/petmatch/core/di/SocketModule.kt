package com.example.petmatch.core.di

import com.example.petmatch.core.socket.PetMatchSocketManager
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import com.example.petmatch.features.health.data.datasources.local.dao.HealthDao
import com.example.petmatch.features.interest.data.datasources.local.dao.InterestDao // Nuevo import
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
        healthDao: HealthDao,
        interestDao: InterestDao // Inyectamos el nuevo DAO de intereses
    ): PetMatchSocketManager {
        return PetMatchSocketManager(petMatchDao, healthDao, interestDao)
    }
}