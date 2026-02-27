package com.example.petmatch.features.petmatch.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petmatch.core.session.UserSession
import com.example.petmatch.features.petmatch.domain.usescases.AssignPetUseCase
import com.example.petmatch.features.petmatch.domain.usescases.GetHomesUseCase
import com.example.petmatch.features.petmatch.domain.usescases.GetPetsUseCase
import com.example.petmatch.features.petmatch.domain.usescases.RefreshDataUseCase
import com.example.petmatch.features.petmatch.presentation.screens.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPetsUseCase: GetPetsUseCase,
    private val getHomesUseCase: GetHomesUseCase,
    private val refreshDataUseCase: RefreshDataUseCase,
    private val assignPetUseCase: AssignPetUseCase, // Añadido para la asignación
    private val userSession: UserSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    val isAdmin: Boolean = userSession.isAdmin()
    val isVoluntario: Boolean = userSession.isVoluntario()

    init {
        observeLocalData()
        loadData()
    }

    private fun observeLocalData() {
        viewModelScope.launch {
            getPetsUseCase().collect { pets ->
                _uiState.update { it.copy(mascotas = pets) }
            }
        }
        viewModelScope.launch {
            getHomesUseCase().collect { homes ->
                _uiState.update { it.copy(hogares = homes) }
            }
        }
    }

    fun loadData() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                refreshDataUseCase()
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _errorFlow.emit("Sin conexión. Mostrando datos locales.")
            }
        }
    }

    // Esta es la función que faltaba y causaba el error de compilación
    fun assignPetToHome(petId: Int, homeId: Int, currentOccupancy: Int) {
        if (!isAdmin) return // Verificación de seguridad básica

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = assignPetUseCase(petId, homeId, currentOccupancy)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    // Al usar Flow en observeLocalData, la UI se actualizará sola
                    // cuando el repositorio actualice la base de datos local.
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(isLoading = false) }
                    _errorFlow.emit(exception.message ?: "Error al asignar el hogar")
                }
            )
        }
    }
}