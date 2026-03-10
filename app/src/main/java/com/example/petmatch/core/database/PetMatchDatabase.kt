package com.example.petmatch.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import com.example.petmatch.features.petmatch.data.datasources.local.entities.*
import com.example.petmatch.features.health.data.datasources.local.dao.HealthDao
import com.example.petmatch.features.health.data.datasources.local.entities.HealthEntity
// Importes de Interest
import com.example.petmatch.features.interest.data.datasources.local.dao.InterestDao
import com.example.petmatch.features.interest.data.datasources.local.entities.InterestEntity

@Database(
    entities = [
        PetEntity::class,
        HomeEntity::class,
        HealthEntity::class,
        InterestEntity::class // Nueva tabla
    ],
    version = 3, // Incrementamos a 3
    exportSchema = false
)
abstract class PetMatchDatabase : RoomDatabase() {
    abstract fun petMatchDao(): PetMatchDao
    abstract fun healthDao(): HealthDao
    abstract fun interestDao(): InterestDao // Nuevo DAO
}