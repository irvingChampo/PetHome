package com.example.petmatch.features.petmatch.data.datasources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.petmatch.features.petmatch.domain.entities.Pet

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val especie: String,
    val edad: Int,
    val estadoSalud: String,
    val estado: String,
    val fotoUrl: String?,
    val isInterested: Boolean = false // NUEVO: Estado del corazón para el usuario actual
)

// Actualizamos los Mappers
fun PetEntity.toDomain() = Pet(id, nombre, especie, edad, estadoSalud, estado, fotoUrl, isInterested)
fun Pet.toEntity() = PetEntity(id, nombre, especie, edad, estadoSalud, estado, fotoUrl, isInterested)