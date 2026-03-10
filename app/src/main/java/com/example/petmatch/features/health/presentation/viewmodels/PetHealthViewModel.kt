package com.example.petmatch.features.health.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petmatch.core.session.UserSession
import com.example.petmatch.features.health.domain.usecases.AddHealthRecordUseCase
import com.example.petmatch.features.health.domain.usecases.GetPetHealthUseCase
import com.example.petmatch.features.health.domain.usecases.RefreshHealthUseCase
import com.example.petmatch.features.health.presentation.screens.HealthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetHealthViewModel @Inject constructor(
    private val getPetHealthUseCase: GetPetHealthUseCase,
    private val refreshHealthUseCase: RefreshHealthUseCase,
    private val addHealthRecordUseCase: AddHealthRecordUseCase,
    private val userSession: UserSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    // Campos del formulario para agregar registro
    private val _diagnostico = MutableStateFlow("")
    val diagnostico = _diagnostico.asStateFlow()

    private val _vacuna = MutableStateFlow("")
    val vacuna = _vacuna.asStateFlow()

    val isAdmin: Boolean = userSession.isAdmin()

    fun updateDiagnostico(value: String) { _diagnostico.value = value }
    fun updateVacuna(value: String) { _vacuna.value = value }

    /**
     * Inicia la observación del historial local y dispara la carga desde la API
     */
    fun loadHistory(petId: Int) {
        // 1. Observar cambios en Room (Tiempo Real)
        viewModelScope.launch {
            getPetHealthUseCase(petId).collect { list ->
                _uiState.update { it.copy(history = list) }
            }
        }

        // 2. Refrescar desde la API
        refresh(petId)
    }

    fun refresh(petId: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                refreshHealthUseCase(petId)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _errorFlow.emit("Error al sincronizar: ${e.message}")
            }
        }
    }

    fun saveRecord(petId: Int, fecha: String) {
        if (_diagnostico.value.isBlank()) {
            viewModelScope.launch { _errorFlow.emit("El diagnóstico es obligatorio") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            addHealthRecordUseCase(
                mascotaId = petId,
                diagnostico = _diagnostico.value,
                vacuna = _vacuna.value.ifBlank { null },
                fecha = fecha
            ).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    _diagnostico.value = ""
                    _vacuna.value = ""
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _errorFlow.emit(e.message ?: "Error al guardar registro")
                }
            )
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}