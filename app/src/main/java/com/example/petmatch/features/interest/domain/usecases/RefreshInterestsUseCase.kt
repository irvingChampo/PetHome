package com.example.petmatch.features.interest.domain.usecases

import com.example.petmatch.features.interest.domain.repositories.InterestRepository
import javax.inject.Inject

class RefreshInterestsUseCase @Inject constructor(
    private val repository: InterestRepository
) {
    suspend operator fun invoke(mascotaId: Int) {
        repository.refreshInterests(mascotaId)
    }
}