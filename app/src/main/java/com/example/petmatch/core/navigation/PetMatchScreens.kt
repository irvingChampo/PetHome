package com.example.petmatch.core.navigation

sealed class PetMatchScreens(val route: String) {
    object Login : PetMatchScreens("login")
    object Register : PetMatchScreens("register")
    object Dashboard : PetMatchScreens("dashboard")
    object AddPet : PetMatchScreens("add_pet")
    object AddHome : PetMatchScreens("add_home")

    // Ruta para asignación
    object AssignPet : PetMatchScreens("assign_pet/{petId}/{petName}") {
        fun createRoute(id: Int, name: String) = "assign_pet/$id/$name"
    }

    // RUTAS DE EDICIÓN
    object EditPet : PetMatchScreens("edit_pet/{id}/{name}/{specie}/{age}") {
        fun createRoute(id: Int, name: String, specie: String, age: Int) =
            "edit_pet/$id/$name/$specie/$age"
    }

    object EditHome : PetMatchScreens("edit_home/{id}/{name}/{dir}/{cap}/{type}") {
        fun createRoute(id: Int, name: String, dir: String, cap: Int, type: String) =
            "edit_home/$id/$name/$dir/$cap/$type"
    }
}