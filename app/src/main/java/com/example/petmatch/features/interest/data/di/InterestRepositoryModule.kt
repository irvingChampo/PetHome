package com.example.petmatch.features.interest.data.di

import com.example.petmatch.features.interest.data.repositories.InterestRepositoryImpl
import com.example.petmatch.features.interest.domain.repositories.InterestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterestRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindInterestRepository(
        interestRepositoryImpl: InterestRepositoryImpl
    ): InterestRepository
}