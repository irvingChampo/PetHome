package com.example.petmatch.core.network

import com.example.petmatch.features.auth.data.datasources.remote.model.*
import com.example.petmatch.features.petmatch.data.datasources.remote.model.*
import retrofit2.http.*

interface PetMatchApi {

    // Mascotas
    @GET("mascotas")
    suspend fun getMascotas(): List<MascotaDto>
    @GET("mascotas/{id}")
    suspend fun getMascotaById(@Path("id") id: Int): MascotaDto
    @POST("mascotas")
    suspend fun crearMascota(@Body mascota: MascotaDto): MascotaDto
    @PUT("mascotas/{id}")
    suspend fun actualizarMascota(@Path("id") id: Int, @Body datos: MascotaDto): MascotaDto
    @DELETE("mascotas/{id}")
    suspend fun eliminarMascota(@Path("id") id: Int)

    // Hogares
    @GET("hogares")
    suspend fun getHogares(): List<HogarDto>
    @GET("hogares/{id}")
    suspend fun getHogarById(@Path("id") id: Int): HogarDto
    @POST("hogares")
    suspend fun crearHogar(@Body hogar: HogarDto): HogarDto
    @PUT("hogares/{id}")
    suspend fun actualizarHogar(@Path("id") id: Int, @Body datos: HogarDto): HogarDto
    @DELETE("hogares/{id}")
    suspend fun eliminarHogar(@Path("id") id: Int)

    // Asignación de hogares
    @PUT("mascotas/{id}")
    suspend fun patchMascota(@Path("id") id: Int, @Body datos: Map<String, String>): MascotaDto
    @PUT("hogares/{id}")
    suspend fun patchHogar(@Path("id") id: Int, @Body datos: Map<String, Int>): HogarDto
}