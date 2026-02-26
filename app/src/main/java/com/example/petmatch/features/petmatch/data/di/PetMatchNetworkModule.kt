package com.example.petmatch.features.petmatch.data.di

import com.example.petmatch.core.di.PetMatchRetrofit
import com.example.petmatch.features.petmatch.data.datasources.remote.api.PetMatchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PetMatchNetworkModule {
    @Provides
    @Singleton
    fun providePetMatchApi(@PetMatchRetrofit retrofit: Retrofit): PetMatchApi {
        return retrofit.create(PetMatchApi::class.java)
    }
}