package com.example.petmatch.features.petmatch.domain.repositories

import com.example.petmatch.features.petmatch.domain.entities.Pet
import com.example.petmatch.features.petmatch.domain.entities.Home
import kotlinx.coroutines.flow.Flow

interface PetMatchRepository {
    fun getPets(): Flow<List<Pet>>
    fun getHomes(): Flow<List<Home>>

    suspend fun refreshPets()
    suspend fun refreshHomes()

    suspend fun createPet(pet: Pet): Pet
    suspend fun updatePet(pet: Pet): Pet
    suspend fun deletePet(id: Int)

    suspend fun createHome(home: Home, telefono: String): Home
    suspend fun updateHome(home: Home, telefono: String): Home
    suspend fun deleteHome(id: Int)

    suspend fun assignPetToHome(petId: Int, homeId: Int, currentOccupancy: Int): Boolean
}