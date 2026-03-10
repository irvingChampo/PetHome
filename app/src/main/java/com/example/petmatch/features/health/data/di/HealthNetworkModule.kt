package com.example.petmatch.features.health.data.di

import com.example.petmatch.core.di.PetMatchRetrofit
import com.example.petmatch.features.health.data.datasources.remote.api.HealthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HealthNetworkModule {

    @Provides
    @Singleton
    fun provideHealthApi(@PetMatchRetrofit retrofit: Retrofit): HealthApi {
        return retrofit.create(HealthApi::class.java)
    }
}