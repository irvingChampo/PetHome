package com.example.petmatch.core.di

import android.content.Context
import androidx.room.Room
import com.example.petmatch.core.database.PetMatchDatabase
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PetMatchDatabase {
        return Room.databaseBuilder(
            context,
            PetMatchDatabase::class.java,
            "petmatch_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePetMatchDao(database: PetMatchDatabase): PetMatchDao {
        return database.petMatchDao()
    }
}