package com.example.petmatch.features.auth.data.repositories

import com.example.petmatch.features.auth.data.datasources.remote.api.AuthApi
import com.example.petmatch.features.auth.data.datasources.remote.model.*
import com.example.petmatch.features.auth.domain.entities.User
import com.example.petmatch.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun login(email: String, pass: String): Result<User> = try {
        val response = api.login(LoginRequest(email, pass))
        Result.success(response.toDomain())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun registro(n: String, e: String, p: String, t: String): Result<User> = try {
        val response = api.registro(RegisterRequest(n, e, p, t))
        Result.success(response.toDomain())
    } catch (e: Exception) {
        Result.failure(e)
    }
}