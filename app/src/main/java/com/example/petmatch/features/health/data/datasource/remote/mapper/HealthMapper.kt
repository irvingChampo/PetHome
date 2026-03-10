package com.example.petmatch.features.health.data.datasources.remote.mapper

import com.example.petmatch.features.health.data.datasources.remote.model.HealthDto
import com.example.petmatch.features.health.domain.entities.Health

/**
 * Convierte un HealthDto (API) a Health (Dominio)
 */
fun HealthDto.toDomain(): Health {
    return Health(
        id = this.id,
        mascotaId = this.mascotaId,
        diagnostico = this.diagnostico,
        vacuna = this.vacuna,
        fechaTratamiento = this.fechaTratamiento
    )
}

/**
 * Convierte una lista de objetos de la API a una lista de dominio
 */
fun List<HealthDto>.toDomainList(): List<Health> {
    return this.map { it.toDomain() }
}