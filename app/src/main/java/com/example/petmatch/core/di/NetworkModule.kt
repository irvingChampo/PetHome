package com.example.petmatch.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @PetMatchRetrofit
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            // Ponemos la URL directamente aquí
            .baseUrl("https://backend-petmatch-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}