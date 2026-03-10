package com.example.petmatch.features.interest.data.datasources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.petmatch.features.interest.domain.entities.Interest

@Entity(tableName = "pet_interests")
data class InterestEntity(
    @PrimaryKey val id: Int,
    val usuarioId: Int,
    val mascotaId: Int,
    val fecha: String,
    val nombreUsuario: String?,
    val emailUsuario: String?,
    val telefonoUsuario: String?
)

fun InterestEntity.toDomain() = Interest(
    id, usuarioId, mascotaId, fecha, nombreUsuario, emailUsuario, telefonoUsuario
)

fun Interest.toEntity() = InterestEntity(
    id, usuarioId, mascotaId, fecha, nombreUsuario, emailUsuario, telefonoUsuario
)