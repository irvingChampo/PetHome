package com.example.petmatch.features.health.domain.entities

data class Health(
    val id: Int,
    val mascotaId: Int,
    val diagnostico: String,
    val vacuna: String?,
    val fechaTratamiento: String
)