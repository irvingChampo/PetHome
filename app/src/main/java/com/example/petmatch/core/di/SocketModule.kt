package com.example.petmatch.core.di

import com.example.petmatch.core.socket.PetMatchSocketManager
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
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
    fun provideSocketManager(petMatchDao: PetMatchDao): PetMatchSocketManager {
        return PetMatchSocketManager(petMatchDao)
    }
}