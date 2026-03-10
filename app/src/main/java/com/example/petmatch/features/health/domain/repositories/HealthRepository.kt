package com.example.petmatch.features.health.domain.repositories

import com.example.petmatch.features.health.domain.entities.Health
import kotlinx.coroutines.flow.Flow

interface HealthRepository {
    // Obtener historial desde Room (con Flow para tiempo real)
    fun getHealthHistory(mascotaId: Int): Flow<List<Health>>

    // Sincronizar datos del servidor a Room
    suspend fun refreshHealthHistory(mascotaId: Int)

    // Agregar nuevo registro (Solo Admin)
    suspend fun addHealthRecord(
        rol: String,
        mascotaId: Int,
        diagnostico: String,
        vacuna: String?,
        fecha: String
    ): Result<Health>
}