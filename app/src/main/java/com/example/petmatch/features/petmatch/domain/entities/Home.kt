package com.example.petmatch.features.petmatch.domain.entities

data class Home(
    val id: Int,
    val nombreVoluntario: String,
    val direccion: String,
    val capacidad: Int,
    val ocupacionActual: Int,
    val tipoMascotaAceptada: String
)