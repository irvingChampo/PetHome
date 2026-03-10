package com.example.petmatch.features.interest.domain.usecases

import com.example.petmatch.features.interest.domain.entities.Interest
import com.example.petmatch.features.interest.domain.repositories.InterestRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInterestsByPetUseCase @Inject constructor(
    private val repository: InterestRepository
) {
    operator fun invoke(mascotaId: Int): Flow<List<Interest>> {
        return repository.getInterestsByPet(mascotaId)
    }
}