package com.example.petmatch.features.interest.domain.repositories

import com.example.petmatch.features.interest.domain.entities.Interest
import kotlinx.coroutines.flow.Flow

interface InterestRepository {

    /**
     * Registra el interés del usuario actual en una mascota.
     * Sincroniza con el servidor y actualiza el estado local.
     */
    suspend fun toggleInterest(usuarioId: Int, mascotaId: Int, isCurrentlyInterested: Boolean): Result<Unit>

    /**
     * Obtiene la lista de personas interesadas en una mascota específica.
     * (Principalmente para uso del Administrador).
     */
    fun getInterestsByPet(mascotaId: Int): Flow<List<Interest>>

    /**
     * Fuerza la actualización de la lista de interesados desde el servidor.
     */
    suspend fun refreshInterests(mascotaId: Int)
}