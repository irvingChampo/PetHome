package com.example.petmatch.features.petmatch.domain.entities

data class Pet(
    val id: Int,
    val nombre: String,
    val especie: String,
    val edad: Int,
    val estadoSalud: String,
    val estado: String,
    val fotoUrl: String?
)