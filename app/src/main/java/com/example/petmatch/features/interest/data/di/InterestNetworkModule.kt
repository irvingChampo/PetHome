package com.example.petmatch.features.interest.data.di

import com.example.petmatch.core.di.PetMatchRetrofit
import com.example.petmatch.features.interest.data.datasources.remote.api.InterestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterestNetworkModule {

    @Provides
    @Singleton
    fun provideInterestApi(@PetMatchRetrofit retrofit: Retrofit): InterestApi {
        return retrofit.create(InterestApi::class.java)
    }
}