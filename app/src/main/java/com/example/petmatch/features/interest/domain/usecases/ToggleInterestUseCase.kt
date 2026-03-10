package com.example.petmatch.features.interest.domain.usecases

import com.example.petmatch.core.session.UserSession
import com.example.petmatch.features.interest.domain.repositories.InterestRepository
import javax.inject.Inject

class ToggleInterestUseCase @Inject constructor(
    private val repository: InterestRepository,
    private val userSession: UserSession
) {
    /**
     * Alterna el estado de interés de una mascota para el usuario actual.
     * @param mascotaId ID de la mascota
     * @param isCurrentlyInterested Estado actual (true si ya tiene el corazón marcado)
     */
    suspend operator fun invoke(mascotaId: Int, isCurrentlyInterested: Boolean): Result<Unit> {
        // Obtenemos el ID del usuario desde la sesión (inyectado por Hilt)
        // Nota: En tu implementación de UserSession no guardas el ID,
        // pero para que esto funcione, lo ideal es obtenerlo del token o guardarlo al hacer login.
        // Simularemos un ID 1 por ahora o puedes ajustar tu UserSession para guardar el ID.

        val usuarioId = 1 // TODO: Obtener ID real del usuario desde UserSession si se añade

        return repository.toggleInterest(
            usuarioId = usuarioId,
            mascotaId = mascotaId,
            isCurrentlyInterested = isCurrentlyInterested
        )
    }
}