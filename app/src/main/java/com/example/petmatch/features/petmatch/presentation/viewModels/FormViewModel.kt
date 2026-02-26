package com.example.petmatch.features.petmatch.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petmatch.features.petmatch.domain.entities.Home
import com.example.petmatch.features.petmatch.domain.entities.Pet
import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import com.example.petmatch.features.petmatch.domain.usecases.AssignPetUseCase
import com.example.petmatch.features.petmatch.presentation.screens.FormUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormViewModel(
    private val repository: PetMatchRepository,
    private val assignPetUseCase: AssignPetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState = _uiState.asStateFlow()

    // HOT FLOWS PARA EL FORMULARIO DE MASCOTAS
    private val _petNombre = MutableStateFlow("")
    val petNombre = _petNombre.asStateFlow()

    private val _petEspecie = MutableStateFlow("Perro")
    val petEspecie = _petEspecie.asStateFlow()

    private val _petEdad = MutableStateFlow("")
    val petEdad = _petEdad.asStateFlow()

    fun updatePetNombre(nombre: String) { _petNombre.value = nombre }
    fun updatePetEspecie(especie: String) { _petEspecie.value = especie }
    fun updatePetEdad(edad: String) { _petEdad.value = edad }

    fun loadInitialPetData(nombre: String, especie: String, edad: String) {
        _petNombre.value = nombre
        _petEspecie.value = if(especie.isNotEmpty()) especie else "Perro"
        _petEdad.value = edad
    }

    fun savePet(id: Int = 0) {
        val nombre = _petNombre.value
        val especie = _petEspecie.value
        val edad = _petEdad.value

        if (nombre.isBlank() || especie.isBlank() || edad.isBlank()) {
            _uiState.update { it.copy(error = "Rellena todos los campos") }
            return
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val pet = Pet(id, nombre.trim(), especie, edad.toIntOrNull() ?: 0, "Saludable", "Sin hogar", null)
                if (id == 0) repository.createPet(pet) else repository.updatePet(pet)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun deletePet(id: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                repository.deletePet(id)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "No se pudo eliminar") }
            }
        }
    }

    // HOT FLOWS PARA EL FORMULARIO DE HOGARES
    private val _homeNombre = MutableStateFlow("")
    val homeNombre = _homeNombre.asStateFlow()

    private val _homeDireccion = MutableStateFlow("")
    val homeDireccion = _homeDireccion.asStateFlow()

    private val _homeTelefono = MutableStateFlow("")
    val homeTelefono = _homeTelefono.asStateFlow()

    private val _homeCapacidad = MutableStateFlow("")
    val homeCapacidad = _homeCapacidad.asStateFlow()

    private val _homeTipo = MutableStateFlow("Perros")
    val homeTipo = _homeTipo.asStateFlow()


    fun updateHomeNombre(nombre: String) { _homeNombre.value = nombre }
    fun updateHomeDireccion(direccion: String) { _homeDireccion.value = direccion }
    fun updateHomeTelefono(telefono: String) { _homeTelefono.value = telefono }
    fun updateHomeCapacidad(capacidad: String) { _homeCapacidad.value = capacidad }
    fun updateHomeTipo(tipo: String) { _homeTipo.value = tipo }

    fun loadInitialHomeData(nombre: String, direccion: String, capacidad: String, tipo: String) {
        _homeNombre.value = nombre
        _homeDireccion.value = direccion
        _homeCapacidad.value = capacidad
        _homeTipo.value = if(tipo.isNotEmpty()) tipo else "Perros"
        _homeTelefono.value = ""
    }

    fun saveHome(id: Int = 0) {
        val nombre = _homeNombre.value
        val direccion = _homeDireccion.value
        val telefono = _homeTelefono.value
        val capacidad = _homeCapacidad.value
        val tipo = _homeTipo.value

        if (nombre.isBlank() || direccion.isBlank() || telefono.isBlank() || capacidad.isBlank()) {
            _uiState.update { it.copy(error = "Campos obligatorios vacíos") }
            return
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val home = Home(id, nombre.trim(), direccion.trim(), capacidad.toIntOrNull() ?: 0, 0, tipo)
                if (id == 0) repository.createHome(home, telefono) else repository.updateHome(home, telefono)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun deleteHome(id: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                repository.deleteHome(id)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error al eliminar hogar") }
            }
        }
    }

    fun assignPetToHome(petId: Int, homeId: Int, currentOccupancy: Int) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = assignPetUseCase(petId, homeId, currentOccupancy)
            result.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isSuccess = true) } },
                onFailure = { error -> _uiState.update { it.copy(isLoading = false, error = error.message) } }
            )
        }
    }

    fun resetState() {
        _uiState.update { FormUiState() }
        _petNombre.value = ""
        _petEspecie.value = "Perro"
        _petEdad.value = ""

        _homeNombre.value = ""
        _homeDireccion.value = ""
        _homeTelefono.value = ""
        _homeCapacidad.value = ""
        _homeTipo.value = "Perros"
    }
}