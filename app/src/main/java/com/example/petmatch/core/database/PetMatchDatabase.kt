package com.example.petmatch.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import com.example.petmatch.features.petmatch.data.datasources.local.entities.HomeEntity
import com.example.petmatch.features.petmatch.data.datasources.local.entities.PetEntity

@Database(entities = [PetEntity::class, HomeEntity::class], version = 1, exportSchema = false)
abstract class PetMatchDatabase : RoomDatabase() {
    abstract fun petMatchDao(): PetMatchDao
}