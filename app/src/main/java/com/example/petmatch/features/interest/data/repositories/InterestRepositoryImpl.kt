package com.example.petmatch.features.interest.data.repositories

import com.example.petmatch.features.interest.data.datasources.local.dao.InterestDao
import com.example.petmatch.features.interest.data.datasources.local.entities.toDomain
import com.example.petmatch.features.interest.data.datasources.local.entities.toEntity
import com.example.petmatch.features.interest.data.datasources.remote.api.InterestApi
import com.example.petmatch.features.interest.data.datasources.remote.mapper.toDomain
import com.example.petmatch.features.interest.data.datasources.remote.model.InterestRequest
import com.example.petmatch.features.interest.domain.entities.Interest
import com.example.petmatch.features.interest.domain.repositories.InterestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InterestRepositoryImpl @Inject constructor(
    private val api: InterestApi,
    private val dao: InterestDao
) : InterestRepository {

    override suspend fun toggleInterest(
        usuarioId: Int,
        mascotaId: Int,
        isCurrentlyInterested: Boolean
    ): Result<Unit> = try {
        // 1. Actualización Optimista: Cambiamos el estado en Room de inmediato
        val newStatus = !isCurrentlyInterested
        dao.updatePetInterestStatus(mascotaId, newStatus)

        // 2. Sincronizar con el servidor
        // Nota: Según la API actual, solo registramos interés.
        // Si ya estaba interesado, en un sistema real haríamos DELETE,
        // pero aquí seguiremos el flujo de la Issue: Registrar el interés.
        if (newStatus) {
            api.registrarInteres(InterestRequest(usuarioId, mascotaId))
        }

        Result.success(Unit)
    } catch (e: Exception) {
        // 3. Rollback: Si falla el servidor, revertimos el cambio local
        dao.updatePetInterestStatus(mascotaId, isCurrentlyInterested)
        Result.failure(e)
    }

    override fun getInterestsByPet(mascotaId: Int): Flow<List<Interest>> {
        return dao.getInterestsByPetFlow(mascotaId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshInterests(mascotaId: Int) {
        val remoteInterests = api.getInteresadosPorMascota(mascotaId)
        dao.deleteInterestsByPet(mascotaId)
        dao.insertInterests(remoteInterests.map { it.toDomain().toEntity() })
    }
}