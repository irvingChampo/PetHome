package com.example.petmatch.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class PetMatchScreens {
    @Serializable object Login
    @Serializable object Register
    @Serializable object Dashboard
    @Serializable object AddPet
    @Serializable object AddHome

    @Serializable
    data class EditPet(val id: Int, val name: String, val specie: String, val age: Int)

    @Serializable
    data class EditHome(val id: Int, val name: String, val dir: String, val cap: Int, val type: String)

    @Serializable
    data class AssignPet(val petId: Int, val petName: String)

    @Serializable
    data class HealthHistory(val petId: Int, val petName: String)

    // NUEVA RUTA: Ver interesados en una mascota
    @Serializable
    data class PetInterests(val petId: Int, val petName: String)
}