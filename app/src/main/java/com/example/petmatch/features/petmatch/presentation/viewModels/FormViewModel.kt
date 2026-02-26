package com.example.petmatch.features.petmatch.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petmatch.features.petmatch.domain.entities.Home
import com.example.petmatch.features.petmatch.domain.entities.Pet
import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import com.example.petmatch.features.petmatch.domain.usescases.AssignPetUseCase
import com.example.petmatch.features.petmatch.presentation.screens.FormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val repository: PetMatchRepository,
    private val assignPetUseCase: AssignPetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState = _uiState.asStateFlow()

    private val _petNombre = MutableStateFlow("")
    val petNombre = _petNombre.asStateFlow()
    private val _petEspecie = MutableStateFlow("Perro")
    val petEspecie = _petEspecie.asStateFlow()
    private val _petEdad = MutableStateFlow("")
    val petEdad = _petEdad.asStateFlow()

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

    fun updatePetNombre(v: String) { _petNombre.value = v }
    fun updatePetEspecie(v: String) { _petEspecie.value = v }
    fun updatePetEdad(v: String) { _petEdad.value = v }
    fun updateHomeNombre(v: String) { _homeNombre.value = v }
    fun updateHomeDireccion(v: String) { _homeDireccion.value = v }
    fun updateHomeTelefono(v: String) { _homeTelefono.value = v }
    fun updateHomeCapacidad(v: String) { _homeCapacidad.value = v }
    fun updateHomeTipo(v: String) { _homeTipo.value = v }

    fun loadInitialPetData(n: String, s: String, a: String) {
        _petNombre.value = n; _petEspecie.value = s; _petEdad.value = a
    }

    fun loadInitialHomeData(n: String, d: String, c: String, t: String) {
        _homeNombre.value = n; _homeDireccion.value = d; _homeCapacidad.value = c; _homeTipo.value = t
    }

    fun savePet(id: Int = 0) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val pet = Pet(id, _petNombre.value, _petEspecie.value, _petEdad.value.toIntOrNull() ?: 0, "Saludable", "Sin hogar", null)
                if (id == 0) repository.createPet(pet) else repository.updatePet(pet)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) { _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    fun saveHome(id: Int = 0) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val home = Home(id, _homeNombre.value, _homeDireccion.value, _homeCapacidad.value.toIntOrNull() ?: 0, 0, _homeTipo.value)
                if (id == 0) repository.createHome(home, _homeTelefono.value) else repository.updateHome(home, _homeTelefono.value)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) { _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    fun deletePet(id: Int) = viewModelScope.launch { repository.deletePet(id) }
    fun deleteHome(id: Int) = viewModelScope.launch { repository.deleteHome(id) }

    fun assignPetToHome(petId: Int, homeId: Int, currentOccupancy: Int) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = assignPetUseCase(petId, homeId, currentOccupancy)
            result.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isSuccess = true) } },
                // Cambiado 'error' por 'exception' para evitar conflicto con FormUiState.error
                onFailure = { exception -> _uiState.update { it.copy(isLoading = false, error = exception.message) } }
            )
        }
    }

    fun resetState() { _uiState.update { FormUiState() } }
}