package com.example.petmatch.features.petmatch.data.datasources.remote.api

import com.example.petmatch.features.petmatch.data.datasources.remote.model.HogarDto
import com.example.petmatch.features.petmatch.data.datasources.remote.model.MascotaDto
import retrofit2.http.*

interface PetMatchApi {
    @GET("mascotas")
    suspend fun getMascotas(): List<MascotaDto>

    @POST("mascotas")
    suspend fun crearMascota(@Body mascota: MascotaDto): MascotaDto

    @PUT("mascotas/{id}")
    suspend fun actualizarMascota(@Path("id") id: Int, @Body datos: MascotaDto): MascotaDto

    @DELETE("mascotas/{id}")
    suspend fun eliminarMascota(@Path("id") id: Int)

    @GET("hogares")
    suspend fun getHogares(): List<HogarDto>

    @POST("hogares")
    suspend fun crearHogar(@Body hogar: HogarDto): HogarDto

    @PUT("hogares/{id}")
    suspend fun actualizarHogar(@Path("id") id: Int, @Body datos: HogarDto): HogarDto

    @DELETE("hogares/{id}")
    suspend fun eliminarHogar(@Path("id") id: Int)

    @PUT("mascotas/{id}")
    suspend fun patchMascota(@Path("id") id: Int, @Body datos: Map<String, String>): MascotaDto

    @PUT("hogares/{id}")
    suspend fun patchHogar(@Path("id") id: Int, @Body datos: Map<String, Int>): HogarDto
}