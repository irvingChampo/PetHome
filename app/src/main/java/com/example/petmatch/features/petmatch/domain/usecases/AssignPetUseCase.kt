package com.example.petmatch.features.petmatch.domain.usescases

import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import javax.inject.Inject

class AssignPetUseCase @Inject constructor(
    private val repository: PetMatchRepository
) {
    suspend operator fun invoke(petId: Int, homeId: Int, currentOccupancy: Int): Result<Boolean> {
        return try {
            val success = repository.assignPetToHome(petId, homeId, currentOccupancy)
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}