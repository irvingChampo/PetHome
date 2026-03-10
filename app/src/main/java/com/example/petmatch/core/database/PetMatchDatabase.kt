package com.example.petmatch.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import com.example.petmatch.features.petmatch.data.datasources.local.entities.HomeEntity
import com.example.petmatch.features.petmatch.data.datasources.local.entities.PetEntity
// Imports de la nueva feature
import com.example.petmatch.features.health.data.datasources.local.dao.HealthDao
import com.example.petmatch.features.health.data.datasources.local.entities.HealthEntity

@Database(
    entities = [
        PetEntity::class,
        HomeEntity::class,
        HealthEntity::class // Nueva tabla
    ],
    version = 2, // Incrementamos versión
    exportSchema = false
)
abstract class PetMatchDatabase : RoomDatabase() {
    abstract fun petMatchDao(): PetMatchDao
    abstract fun healthDao(): HealthDao // Nuevo acceso al DAO de salud
}