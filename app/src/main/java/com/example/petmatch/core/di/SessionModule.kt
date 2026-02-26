package com.example.petmatch.core.di

import com.example.petmatch.core.session.UserSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Provides
    @Singleton
    fun provideUserSession(): UserSession {
        return UserSession()
    }
}