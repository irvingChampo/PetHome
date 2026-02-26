package com.example.petmatch.features.auth.data.datasources.remote.mapper

import com.example.petmatch.features.auth.data.datasources.remote.model.AuthResponse
import com.example.petmatch.features.auth.domain.entities.User

/**
 * Convierte el DTO de la API (AuthResponse)
 * al modelo de dominio (User) incluyendo el Rol.
 */
fun AuthResponse.toDomain(): User {
    return User(
        token = this.token,
        role = this.rol ?: "voluntario"
    )
}