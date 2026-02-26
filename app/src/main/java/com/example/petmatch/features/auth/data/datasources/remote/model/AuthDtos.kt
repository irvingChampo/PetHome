package com.example.petmatch.features.auth.data.datasources.remote.model

import com.example.petmatch.features.auth.domain.entities.User

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val nombre: String, val email: String, val password: String, val telefono: String)
data class AuthResponse(val token: String)

fun AuthResponse.toDomain(): User = User(token = this.token)