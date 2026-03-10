package com.example.petmatch.features.interest.data.datasources.local.dao

import androidx.room.*
import com.example.petmatch.features.interest.data.datasources.local.entities.InterestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestDao {
    // Obtener interesados para una mascota (Admin)
    @Query("SELECT * FROM pet_interests WHERE mascotaId = :petId ORDER BY fecha DESC")
    fun getInterestsByPetFlow(petId: Int): Flow<List<InterestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterests(interests: List<InterestEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterest(interest: InterestEntity)

    @Query("DELETE FROM pet_interests WHERE mascotaId = :petId")
    suspend fun deleteInterestsByPet(petId: Int)

    // NUEVO: Toggle visual del corazón en la tabla de mascotas
    @Query("UPDATE pets SET isInterested = :status WHERE id = :petId")
    suspend fun updatePetInterestStatus(petId: Int, status: Boolean)
}