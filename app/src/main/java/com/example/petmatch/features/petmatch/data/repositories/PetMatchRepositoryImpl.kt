package com.example.petmatch.features.petmatch.data.repositories

import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import com.example.petmatch.features.petmatch.data.datasources.local.entities.toDomain
import com.example.petmatch.features.petmatch.data.datasources.local.entities.toEntity
import com.example.petmatch.features.petmatch.data.datasources.remote.api.PetMatchApi
import com.example.petmatch.features.petmatch.data.datasources.remote.model.*
import com.example.petmatch.features.petmatch.domain.entities.Home
import com.example.petmatch.features.petmatch.domain.entities.Pet
import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetMatchRepositoryImpl @Inject constructor(
    private val api: PetMatchApi,
    private val dao: PetMatchDao
) : PetMatchRepository {

    override fun getPets(): Flow<List<Pet>> = dao.getPetsFlow().map { list -> list.map { it.toDomain() } }
    override fun getHomes(): Flow<List<Home>> = dao.getHomesFlow().map { list -> list.map { it.toDomain() } }

    override suspend fun refreshPets() {
        val remotePets = api.getMascotas().map { it.toDomain().toEntity() }
        dao.insertPets(remotePets)
    }

    override suspend fun refreshHomes() {
        val remoteHomes = api.getHogares().map { it.toDomain().toEntity() }
        dao.insertHomes(remoteHomes)
    }

    override suspend fun createPet(pet: Pet): Pet {
        // CORREGIDO: Añadimos fotoUrl al DTO que se envía a la API
        val dto = MascotaDto(
            nombre = pet.nombre,
            especie = pet.especie,
            edad = pet.edad,
            estadoSalud = pet.estadoSalud,
            estado = pet.estado,
            fotoUrl = pet.fotoUrl
        )
        val createdPet = api.crearMascota(dto).toDomain()
        dao.insertPet(createdPet.toEntity())
        return createdPet
    }

    override suspend fun updatePet(pet: Pet): Pet {
        // CORREGIDO: Añadimos fotoUrl al DTO que se envía a la API
        val dto = MascotaDto(
            id = pet.id,
            nombre = pet.nombre,
            especie = pet.especie,
            edad = pet.edad,
            estadoSalud = pet.estadoSalud,
            estado = pet.estado,
            fotoUrl = pet.fotoUrl
        )
        val updatedPet = api.actualizarMascota(pet.id, dto).toDomain()
        dao.insertPet(updatedPet.toEntity())
        return updatedPet
    }

    override suspend fun deletePet(id: Int) {
        api.eliminarMascota(id)
        dao.deletePet(id)
    }

    override suspend fun createHome(home: Home, telefono: String): Home {
        val dto = HogarDto(nombreVoluntario = home.nombreVoluntario, direccion = home.direccion, telefono = telefono, capacidad = home.capacidad, ocupacionActual = home.ocupacionActual, tipoMascotaAceptada = home.tipoMascotaAceptada)
        val createdHome = api.crearHogar(dto).toDomain()
        dao.insertHome(createdHome.toEntity())
        return createdHome
    }

    override suspend fun updateHome(home: Home, telefono: String): Home {
        val dto = HogarDto(id = home.id, nombreVoluntario = home.nombreVoluntario, direccion = home.direccion, telefono = telefono, capacidad = home.capacidad, ocupacionActual = home.ocupacionActual, tipoMascotaAceptada = home.tipoMascotaAceptada)
        val updatedHome = api.actualizarHogar(home.id, dto).toDomain()
        dao.insertHome(updatedHome.toEntity())
        return updatedHome
    }

    override suspend fun deleteHome(id: Int) {
        api.eliminarHogar(id)
        dao.deleteHome(id)
    }

    override suspend fun assignPetToHome(petId: Int, homeId: Int, currentOccupancy: Int): Boolean {
        dao.updatePetState(petId, "En acogida")
        dao.updateHomeOccupancy(homeId, currentOccupancy + 1)

        return try {
            api.patchMascota(petId, mapOf("estado" to "En acogida", "hogarId" to homeId.toString()))
            api.patchHogar(homeId, mapOf("ocupacionActual" to currentOccupancy + 1))
            true
        } catch (e: Exception) {
            dao.updatePetState(petId, "Sin hogar")
            dao.updateHomeOccupancy(homeId, currentOccupancy)
            throw Exception("El servidor falló. Cambios revertidos en pantalla.")
        }
    }
}