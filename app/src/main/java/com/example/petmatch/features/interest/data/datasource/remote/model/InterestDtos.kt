package com.example.petmatch.features.interest.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Request para registrar interés
 */
data class InterestRequest(
    val usuarioId: Int,
    val mascotaId: Int
)

/**
 * Representa al usuario dentro de un objeto Interés (para la lista del Admin)
 */
data class UserSummaryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("telefono") val telefono: String?
)

/**
 * Respuesta de la API para los intereses
 */
data class InterestDto(
    @SerializedName("id") val id: Int,
    @SerializedName("usuarioId") val usuarioId: Int,
    @SerializedName("mascotaId") val mascotaId: Int,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("Usuario") val usuario: UserSummaryDto? = null // Datos anidados del servidor
)