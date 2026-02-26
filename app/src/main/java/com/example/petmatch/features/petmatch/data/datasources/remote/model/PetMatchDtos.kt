package com.example.petmatch.features.petmatch.data.datasources.remote.model

import com.example.petmatch.features.petmatch.domain.entities.Home
import com.example.petmatch.features.petmatch.domain.entities.Pet
import com.google.gson.annotations.SerializedName

data class MascotaDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("especie") val especie: String,
    @SerializedName("edad") val edad: Int,
    @SerializedName("estadoSalud") val estadoSalud: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("fotoUrl") val fotoUrl: String? = null
)

data class HogarDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nombreVoluntario") val nombreVoluntario: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("capacidad") val capacidad: Int,
    @SerializedName("ocupacionActual") val ocupacionActual: Int,
    @SerializedName("tipoMascotaAceptada") val tipoMascotaAceptada: String
)

fun MascotaDto.toDomain(): Pet = Pet(
    id = id ?: 0,
    nombre = nombre,
    especie = especie,
    edad = edad,
    estadoSalud = estadoSalud,
    estado = estado,
    fotoUrl = fotoUrl
)

fun HogarDto.toDomain(): Home = Home(
    id = id ?: 0,
    nombreVoluntario = nombreVoluntario,
    direccion = direccion,
    capacidad = capacidad,
    ocupacionActual = ocupacionActual,
    tipoMascotaAceptada = tipoMascotaAceptada
)