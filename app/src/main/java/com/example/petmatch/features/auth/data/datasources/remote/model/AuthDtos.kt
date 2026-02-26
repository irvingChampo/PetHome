package com.example.petmatch.features.auth.data.datasources.remote.model

import com.example.petmatch.features.auth.domain.entities.User

data class LoginRequest(val email: String, val password: String)

// AÑADIMOS EL CAMPO ROL AQUÍ
data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val telefono: String,
    val rol: String // Enviará "admin" o "voluntario"
)

data class AuthResponse(
    val token: String,
    val rol: String? = "voluntario",
    val nombre: String? = ""
)

fun AuthResponse.toDomain(): User = User(
    token = this.token,
    role = this.rol ?: "voluntario"
)