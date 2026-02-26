package com.example.petmatch.features.petmatch.data.datasources.local.dao

import androidx.room.*
import com.example.petmatch.features.petmatch.data.datasources.local.entities.HomeEntity
import com.example.petmatch.features.petmatch.data.datasources.local.entities.PetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetMatchDao {
    @Query("SELECT * FROM pets")
    fun getPetsFlow(): Flow<List<PetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPets(pets: List<PetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity)

    @Query("DELETE FROM pets WHERE id = :id")
    suspend fun deletePet(id: Int)

    @Query("UPDATE pets SET estado = :nuevoEstado WHERE id = :petId")
    suspend fun updatePetState(petId: Int, nuevoEstado: String)

    @Query("SELECT * FROM homes")
    fun getHomesFlow(): Flow<List<HomeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomes(homes: List<HomeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHome(home: HomeEntity)

    @Query("DELETE FROM homes WHERE id = :id")
    suspend fun deleteHome(id: Int)

    @Query("UPDATE homes SET ocupacionActual = :nuevaOcupacion WHERE id = :homeId")
    suspend fun updateHomeOccupancy(homeId: Int, nuevaOcupacion: Int)
}