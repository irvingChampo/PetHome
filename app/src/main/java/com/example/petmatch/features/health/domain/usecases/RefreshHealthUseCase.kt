package com.example.petmatch.features.health.domain.usecases

import com.example.petmatch.features.health.domain.repositories.HealthRepository
import javax.inject.Inject

class RefreshHealthUseCase @Inject constructor(
    private val repository: HealthRepository
) {
    suspend operator fun invoke(mascotaId: Int) {
        repository.refreshHealthHistory(mascotaId)
    }
}