package com.example.petmatch.features.health.data.repositories

import com.example.petmatch.features.health.data.datasources.local.dao.HealthDao
import com.example.petmatch.features.health.data.datasources.local.entities.toDomain
import com.example.petmatch.features.health.data.datasources.local.entities.toEntity
import com.example.petmatch.features.health.data.datasources.remote.api.HealthApi
import com.example.petmatch.features.health.data.datasources.remote.mapper.toDomain
import com.example.petmatch.features.health.data.datasources.remote.model.HealthRequest
import com.example.petmatch.features.health.domain.entities.Health
import com.example.petmatch.features.health.domain.repositories.HealthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HealthRepositoryImpl @Inject constructor(
    private val api: HealthApi,
    private val dao: HealthDao
) : HealthRepository {

    // Obtenemos de Room y transformamos a Dominio
    override fun getHealthHistory(mascotaId: Int): Flow<List<Health>> {
        return dao.getHealthHistoryFlow(mascotaId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Descargamos de la API y actualizamos Room
    override suspend fun refreshHealthHistory(mascotaId: Int) {
        val remoteHistory = api.getHealthHistory(mascotaId)
        // Limpiamos local e insertamos lo nuevo para mantener consistencia
        dao.deleteHealthHistoryByPet(mascotaId)
        dao.insertHealthRecords(remoteHistory.map { it.toDomain().toEntity() })
    }

    // Enviamos a la API e insertamos el resultado en Room
    override suspend fun addHealthRecord(
        rol: String,
        mascotaId: Int,
        diagnostico: String,
        vacuna: String?,
        fecha: String
    ): Result<Health> = try {
        val request = HealthRequest(rol, mascotaId, diagnostico, vacuna, fecha)
        val responseDto = api.addHealthRecord(request)
        val domainHealth = responseDto.toDomain()

        // Actualizar local
        dao.insertHealthRecord(domainHealth.toEntity())

        Result.success(domainHealth)
    } catch (e: Exception) {
        Result.failure(e)
    }
}