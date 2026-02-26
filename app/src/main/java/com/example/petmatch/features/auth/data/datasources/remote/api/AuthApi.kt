package com.example.petmatch.features.auth.data.datasources.remote.api

import com.example.petmatch.features.auth.data.datasources.remote.model.*
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("usuarios/registro")
    suspend fun registro(@Body request: RegisterRequest): AuthResponse
}