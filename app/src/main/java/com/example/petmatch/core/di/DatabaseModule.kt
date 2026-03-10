package com.example.petmatch.core.di

import android.content.Context
import androidx.room.Room
import com.example.petmatch.core.database.PetMatchDatabase
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import com.example.petmatch.features.health.data.datasources.local.dao.HealthDao // Import nuevo
import com.example.petmatch.features.interest.data.datasources.local.dao.InterestDao
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
        )
            .fallbackToDestructiveMigration() // Ayuda a limpiar la BD si cambias versiones sin migración manual
            .build()
    }

    @Provides
    @Singleton
    fun providePetMatchDao(database: PetMatchDatabase): PetMatchDao {
        return database.petMatchDao()
    }

    // NUEVO: Proveer el DAO de salud para la inyección de dependencias
    @Provides
    @Singleton
    fun provideHealthDao(database: PetMatchDatabase): HealthDao {
        return database.healthDao()
    }

    @Provides
    @Singleton
    fun provideInterestDao(database: PetMatchDatabase): InterestDao {
        return database.interestDao()
    }
}