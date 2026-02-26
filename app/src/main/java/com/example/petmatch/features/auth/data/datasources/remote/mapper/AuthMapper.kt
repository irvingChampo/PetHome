package com.example.petmatch.features.auth.data.datasources.remote.mapper

import com.example.petmatch.features.auth.data.datasources.remote.model.AuthResponse
import com.example.petmatch.features.auth.domain.entities.User

/**
 * Convierte el DTO de la API
 * al modelo de dominio que usa la App.
 */
fun AuthResponse.toDomain(): User {
    return User(
        token = this.token
    )
}