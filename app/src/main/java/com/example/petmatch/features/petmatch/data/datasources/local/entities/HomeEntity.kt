package com.example.petmatch.features.petmatch.data.datasources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.petmatch.features.petmatch.domain.entities.Home

@Entity(tableName = "homes")
data class HomeEntity(
    @PrimaryKey val id: Int,
    val nombreVoluntario: String,
    val direccion: String,
    val capacidad: Int,
    val ocupacionActual: Int,
    val tipoMascotaAceptada: String
)

fun HomeEntity.toDomain() = Home(id, nombreVoluntario, direccion, capacidad, ocupacionActual, tipoMascotaAceptada)
fun Home.toEntity() = HomeEntity(id, nombreVoluntario, direccion, capacidad, ocupacionActual, tipoMascotaAceptada)