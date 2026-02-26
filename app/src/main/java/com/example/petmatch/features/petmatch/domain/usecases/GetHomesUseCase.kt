package com.example.petmatch.features.petmatch.domain.usescases

import com.example.petmatch.features.petmatch.domain.entities.Home
import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import javax.inject.Inject

class GetHomesUseCase @Inject constructor(
    private val repository: PetMatchRepository
) {
    suspend operator fun invoke(): Result<List<Home>> {
        return try {
            Result.success(repository.getHomes())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}