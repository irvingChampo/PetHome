package com.example.petmatch.features.auth.domain.repositories

import com.example.petmatch.features.auth.domain.entities.User

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<User>
    suspend fun registro(nombre: String, email: String, pass: String, tel: String): Result<User>
}