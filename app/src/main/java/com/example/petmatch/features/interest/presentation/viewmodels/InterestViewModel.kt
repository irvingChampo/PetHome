package com.example.petmatch.features.interest.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petmatch.features.interest.domain.usecases.GetInterestsByPetUseCase
import com.example.petmatch.features.interest.domain.usecases.RefreshInterestsUseCase
import com.example.petmatch.features.interest.domain.usecases.ToggleInterestUseCase
import com.example.petmatch.features.interest.presentation.screens.InterestUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestViewModel @Inject constructor(
    private val toggleInterestUseCase: ToggleInterestUseCase,
    private val getInterestsByPetUseCase: GetInterestsByPetUseCase,
    private val refreshInterestsUseCase: RefreshInterestsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterestUiState())
    val uiState = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    /**
     * Acción de presionar el corazón.
     * Se puede llamar desde cualquier pantalla (Dashboard, Detalles, etc.)
     */
    fun toggleInterest(petId: Int, isCurrentlyInterested: Boolean) {
        viewModelScope.launch {
            toggleInterestUseCase(petId, isCurrentlyInterested).fold(
                onSuccess = { /* La UI se actualiza sola vía Room/Flow en DashboardViewModel */ },
                onFailure = { e ->
                    _errorFlow.emit("Error al marcar interés: ${e.message}")
                }
            )
        }
    }

    /**
     * Carga la lista de interesados para el Admin
     */
    fun loadInterests(petId: Int) {
        // 1. Observar Room (Sincronización en tiempo real vía Sockets)
        viewModelScope.launch {
            getInterestsByPetUseCase(petId).collect { list ->
                _uiState.update { it.copy(interests = list) }
            }
        }

        // 2. Refrescar desde el servidor
        refresh(petId)
    }

    fun refresh(petId: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                refreshInterestsUseCase(petId)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _errorFlow.emit("No se pudo actualizar la lista de interesados.")
            }
        }
    }
}