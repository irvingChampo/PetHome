package com.example.petmatch.features.petmatch.data.repositories

import com.example.petmatch.features.petmatch.data.datasources.remote.api.PetMatchApi
import com.example.petmatch.features.petmatch.data.datasources.remote.model.*
import com.example.petmatch.features.petmatch.domain.entities.Home
import com.example.petmatch.features.petmatch.domain.entities.Pet
import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import javax.inject.Inject

class PetMatchRepositoryImpl @Inject constructor(
    private val api: PetMatchApi
) : PetMatchRepository {

    override suspend fun getPets(): List<Pet> = api.getMascotas().map { it.toDomain() }

    override suspend fun getHomes(): List<Home> = api.getHogares().map { it.toDomain() }

    override suspend fun createPet(pet: Pet): Pet {
        val dto = MascotaDto(nombre = pet.nombre, especie = pet.especie, edad = pet.edad, estadoSalud = pet.estadoSalud, estado = pet.estado)
        return api.crearMascota(dto).toDomain()
    }

    override suspend fun updatePet(pet: Pet): Pet {
        val dto = MascotaDto(id = pet.id, nombre = pet.nombre, especie = pet.especie, edad = pet.edad, estadoSalud = pet.estadoSalud, estado = pet.estado)
        return api.actualizarMascota(pet.id, dto).toDomain()
    }

    override suspend fun deletePet(id: Int) = api.eliminarMascota(id)

    override suspend fun createHome(home: Home, telefono: String): Home {
        val dto = HogarDto(nombreVoluntario = home.nombreVoluntario, direccion = home.direccion, telefono = telefono, capacidad = home.capacidad, ocupacionActual = home.ocupacionActual, tipoMascotaAceptada = home.tipoMascotaAceptada)
        return api.crearHogar(dto).toDomain()
    }

    override suspend fun updateHome(home: Home, telefono: String): Home {
        val dto = HogarDto(id = home.id, nombreVoluntario = home.nombreVoluntario, direccion = home.direccion, telefono = telefono, capacidad = home.capacidad, ocupacionActual = home.ocupacionActual, tipoMascotaAceptada = home.tipoMascotaAceptada)
        return api.actualizarHogar(home.id, dto).toDomain()
    }

    override suspend fun deleteHome(id: Int) = api.eliminarHogar(id)

    override suspend fun assignPetToHome(petId: Int, homeId: Int, currentOccupancy: Int): Boolean {
        return try {
            api.patchMascota(petId, mapOf("estado" to "En acogida", "hogarId" to homeId.toString()))
            api.patchHogar(homeId, mapOf("ocupacionActual" to currentOccupancy + 1))
            true
        } catch (e: Exception) {
            false
        }
    }
}