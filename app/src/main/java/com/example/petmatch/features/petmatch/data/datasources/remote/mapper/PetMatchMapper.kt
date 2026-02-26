package com.example.petmatch.features.petmatch.data.datasources.remote.mapper

import com.example.petmatch.features.petmatch.data.datasources.remote.model.*
import com.example.petmatch.features.petmatch.domain.entities.*

fun MascotaDto.toDomain(): Pet = Pet(
    id = this.id ?: 0,
    nombre = this.nombre,
    especie = this.especie,
    edad = this.edad,
    estadoSalud = this.estadoSalud,
    estado = this.estado,
    fotoUrl = this.fotoUrl
)

fun HogarDto.toDomain(): Home = Home(
    id = this.id ?: 0,
    nombreVoluntario = this.nombreVoluntario,
    direccion = this.direccion,
    capacidad = this.capacidad,
    ocupacionActual = this.ocupacionActual,
    tipoMascotaAceptada = this.tipoMascotaAceptada
)