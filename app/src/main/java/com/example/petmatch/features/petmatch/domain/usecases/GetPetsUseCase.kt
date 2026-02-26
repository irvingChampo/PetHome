package com.example.petmatch.features.petmatch.domain.usescases

import com.example.petmatch.features.petmatch.domain.entities.Pet
import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(
    private val repository: PetMatchRepository
) {
    suspend operator fun invoke(): Result<List<Pet>> {
        return try {
            Result.success(repository.getPets())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}