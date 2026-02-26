package com.example.petmatch.features.petmatch.domain.usescases

import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import javax.inject.Inject

class RefreshDataUseCase @Inject constructor(
    private val repository: PetMatchRepository
) {
    suspend operator fun invoke() {
        repository.refreshPets()
        repository.refreshHomes()
    }
}