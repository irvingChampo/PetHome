package com.example.petmatch.features.health.data.datasources.remote.api

import com.example.petmatch.features.health.data.datasources.remote.model.HealthDto
import com.example.petmatch.features.health.data.datasources.remote.model.HealthRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HealthApi {

    // Obtener el historial clínico de una mascota específica
    @GET("salud/mascota/{mascotaId}")
    suspend fun getHealthHistory(
        @Path("mascotaId") mascotaId: Int
    ): List<HealthDto>

    // Agregar un nuevo registro (Solo Admin)
    @POST("salud")
    suspend fun addHealthRecord(
        @Body request: HealthRequest
    ): HealthDto
}