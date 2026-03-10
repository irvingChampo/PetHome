package com.example.petmatch.features.health.domain.usecases

import com.example.petmatch.core.session.UserSession
import com.example.petmatch.features.health.domain.entities.Health
import com.example.petmatch.features.health.domain.repositories.HealthRepository
import javax.inject.Inject

class AddHealthRecordUseCase @Inject constructor(
    private val repository: HealthRepository,
    private val userSession: UserSession // Inyectamos la sesión para validar el rol
) {
    suspend operator fun invoke(
        mascotaId: Int,
        diagnostico: String,
        vacuna: String?,
        fecha: String
    ): Result<Health> {

        // Verificación de seguridad en la capa de dominio
        val rol = userSession.role ?: ""
        if (rol.lowercase() != "admin") {
            return Result.failure(Exception("Acceso denegado. Solo administradores pueden agregar registros."))
        }

        return repository.addHealthRecord(
            rol = rol,
            mascotaId = mascotaId,
            diagnostico = diagnostico,
            vacuna = vacuna,
            fecha = fecha
        )
    }
}