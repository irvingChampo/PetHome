package com.example.petmatch.features.petmatch.domain.usescases

import com.example.petmatch.features.petmatch.domain.entities.Home
import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomesUseCase @Inject constructor(
    private val repository: PetMatchRepository
) {
    operator fun invoke(): Flow<List<Home>> {
        return repository.getHomes()
    }
}