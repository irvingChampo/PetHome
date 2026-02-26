package com.example.petmatch.features.petmatch.domain.repositories

import com.example.petmatch.features.petmatch.domain.entities.Pet
import com.example.petmatch.features.petmatch.domain.entities.Home

interface PetMatchRepository {
    suspend fun getPets(): List<Pet>
    suspend fun getHomes(): List<Home>
    suspend fun createPet(pet: Pet): Pet
    suspend fun updatePet(pet: Pet): Pet
    suspend fun deletePet(id: Int)
    suspend fun createHome(home: Home, telefono: String): Home
    suspend fun updateHome(home: Home, telefono: String): Home
    suspend fun deleteHome(id: Int)
    suspend fun assignPetToHome(petId: Int, homeId: Int, currentOccupancy: Int): Boolean
}