package com.example.petmatch.features.petmatch.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petmatch.core.session.UserSession
import com.example.petmatch.features.petmatch.domain.entities.Home
import com.example.petmatch.features.petmatch.domain.entities.Pet
import com.example.petmatch.features.petmatch.domain.repositories.PetMatchRepository
import com.example.petmatch.features.petmatch.presentation.screens.FormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val repository: PetMatchRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    private val _petNombre = MutableStateFlow("")
    val petNombre = _petNombre.asStateFlow()
    private val _petEspecie = MutableStateFlow("Perro")
    val petEspecie = _petEspecie.asStateFlow()
    private val _petEdad = MutableStateFlow("")
    val petEdad = _petEdad.asStateFlow()

    private val _petImagePath = MutableStateFlow<String?>(null)
    val petImagePath = _petImagePath.asStateFlow()

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
    fun updatePetImagePath(path: String?) { _petImagePath.value = path }
    fun updateHomeNombre(v: String) { _homeNombre.value = v }
    fun updateHomeDireccion(v: String) { _homeDireccion.value = v }
    fun updateHomeTelefono(v: String) { _homeTelefono.value = v }
    fun updateHomeCapacidad(v: String) { _homeCapacidad.value = v }
    fun updateHomeTipo(v: String) { _homeTipo.value = v }

    fun loadInitialPetData(n: String, s: String, a: String, imageUrl: String? = null) {
        _petNombre.value = n; _petEspecie.value = s; _petEdad.value = a

        if (!imageUrl.isNullOrBlank()) {
            val file = File(imageUrl)
            if (file.exists()) {
                _petImagePath.value = imageUrl
            } else {
                _petImagePath.value = null
            }
        } else {
            _petImagePath.value = null
        }
    }

    fun loadInitialHomeData(n: String, d: String, c: String, t: String) {
        _homeNombre.value = n; _homeDireccion.value = d; _homeCapacidad.value = c; _homeTipo.value = t
    }

    fun savePet(id: Int = 0) {
        if (!userSession.isAdmin()) {
            viewModelScope.launch { _errorFlow.emit("Acceso denegado: Se requiere rol Admin") }
            return
        }
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val imageString = _petImagePath.value
                val pet = Pet(id, _petNombre.value, _petEspecie.value, _petEdad.value.toIntOrNull() ?: 0, "Saludable", "Sin hogar", imageString)
                if (id == 0) repository.createPet(pet) else repository.updatePet(pet)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _errorFlow.emit(e.message ?: "Error desconocido")
            }
        }
    }

    fun saveHome(id: Int = 0) {
        if (!userSession.isVoluntario()) {
            viewModelScope.launch { _errorFlow.emit("Acceso denegado: Se requiere rol Voluntario") }
            return
        }
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val home = Home(id, _homeNombre.value, _homeDireccion.value, _homeCapacidad.value.toIntOrNull() ?: 0, 0, _homeTipo.value)
                if (id == 0) repository.createHome(home, _homeTelefono.value) else repository.updateHome(home, _homeTelefono.value)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _errorFlow.emit(e.message ?: "Error desconocido")
            }
        }
    }

    // --- ACTUALIZADO: Bloques try-catch y callback de éxito ---
    fun deletePet(id: Int, onSuccess: () -> Unit = {}) = viewModelScope.launch {
        if (userSession.isAdmin()) {
            try {
                repository.deletePet(id)
                onSuccess() // Le avisamos a la pantalla que ya terminó de borrar
            } catch (e: Exception) {
                _errorFlow.emit("Error al eliminar la mascota: ${e.message}")
            }
        }
    }

    fun deleteHome(id: Int, onSuccess: () -> Unit = {}) = viewModelScope.launch {
        if (userSession.isVoluntario()) {
            try {
                repository.deleteHome(id)
                onSuccess() // Le avisamos a la pantalla que ya terminó de borrar
            } catch (e: Exception) {
                _errorFlow.emit("Error al eliminar el hogar: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.update { FormUiState() }
        _petImagePath.value = null
    }
}