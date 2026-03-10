package com.example.petmatch.features.health.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Request para agregar un nuevo registro de salud.
 * Según la API, requiere el campo 'rol' para validación.
 */
data class HealthRequest(
    val rol: String,
    val mascotaId: Int,
    val diagnostico: String,
    val vacuna: String?,
    val fechaTratamiento: String
)

/**
 * Objeto que devuelve la API al consultar el historial.
 */
data class HealthDto(
    @SerializedName("id") val id: Int,
    @SerializedName("mascotaId") val mascotaId: Int,
    @SerializedName("diagnostico") val diagnostico: String,
    @SerializedName("vacuna") val vacuna: String?,
    @SerializedName("fechaTratamiento") val fechaTratamiento: String,
    @SerializedName("createdAt") val createdAt: String? = null
)