package com.example.petmatch.features.health.domain.usecases

import com.example.petmatch.features.health.domain.entities.Health
import com.example.petmatch.features.health.domain.repositories.HealthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPetHealthUseCase @Inject constructor(
    private val repository: HealthRepository
) {
    // Devuelve el flujo constante de datos de Room
    operator fun invoke(mascotaId: Int): Flow<List<Health>> {
        return repository.getHealthHistory(mascotaId)
    }
}