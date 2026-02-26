package com.example.petmatch.features.petmatch.data.di

import com.example.petmatch.features.petmatch.data.repositories.PetMatchRepositoryImpl
import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPetMatchRepository(
        petMatchRepositoryImpl: PetMatchRepositoryImpl
    ): PetMatchRepository
}