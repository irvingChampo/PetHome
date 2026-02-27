package com.example.petmatch.core.navigation

import kotlinx.serialization.Serializable

// Definimos las rutas como objetos o clases serializables
@Serializable
sealed class PetMatchScreens {
    @Serializable object Login
    @Serializable object Register
    @Serializable object Dashboard
    @Serializable object AddPet
    @Serializable object AddHome

    // Las rutas con parámetros ahora son data classes
    @Serializable
    data class EditPet(
        val id: Int,
        val name: String,
        val specie: String,
        val age: Int
    )

    @Serializable
    data class EditHome(
        val id: Int,
        val name: String,
        val dir: String,
        val cap: Int,
        val type: String
    )

    @Serializable
    data class AssignPet(
        val petId: Int,
        val petName: String
    )
}