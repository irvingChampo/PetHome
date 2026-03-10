package com.example.petmatch.features.interest.data.datasources.remote.api

import com.example.petmatch.features.interest.data.datasources.remote.model.InterestDto
import com.example.petmatch.features.interest.data.datasources.remote.model.InterestRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InterestApi {

    @POST("intereses")
    suspend fun registrarInteres(
        @Body request: InterestRequest
    ): InterestDto

    @GET("intereses/mascota/{mascotaId}")
    suspend fun getInteresadosPorMascota(
        @Path("mascotaId") mascotaId: Int
    ): List<InterestDto>
}