package com.example.petmatch.features.health.data.datasources.local.dao

import androidx.room.*
import com.example.petmatch.features.health.data.datasources.local.entities.HealthEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDao {
    @Query("SELECT * FROM pet_health_history WHERE mascotaId = :petId ORDER BY fechaTratamiento DESC")
    fun getHealthHistoryFlow(petId: Int): Flow<List<HealthEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecord(health: HealthEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecords(healthList: List<HealthEntity>)

    @Query("DELETE FROM pet_health_history WHERE mascotaId = :petId")
    suspend fun deleteHealthHistoryByPet(petId: Int)
}