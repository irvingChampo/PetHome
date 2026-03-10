package com.example.petmatch.features.interest.domain.entities

data class Interest(
    val id: Int,
    val usuarioId: Int,
    val mascotaId: Int,
    val fecha: String,
    // Opcionales para cuando el Admin consulta la lista
    val nombreUsuario: String? = null,
    val emailUsuario: String? = null,
    val telefonoUsuario: String? = null
)