package com.example.petmatch.features.interest.data.datasources.remote.mapper

import com.example.petmatch.features.interest.data.datasources.remote.model.InterestDto
import com.example.petmatch.features.interest.domain.entities.Interest

/**
 * Convierte el DTO de la API a nuestra entidad de Dominio.
 * Si trae datos del usuario (anidados), los extraemos a primer nivel.
 */
fun InterestDto.toDomain(): Interest {
    return Interest(
        id = this.id,
        usuarioId = this.usuarioId,
        mascotaId = this.mascotaId,
        fecha = this.fecha,
        nombreUsuario = this.usuario?.nombre,
        emailUsuario = this.usuario?.email,
        telefonoUsuario = this.usuario?.telefono
    )
}

fun List<InterestDto>.toDomainList(): List<Interest> {
    return this.map { it.toDomain() }
}