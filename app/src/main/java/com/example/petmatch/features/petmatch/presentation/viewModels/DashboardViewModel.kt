package com.example.petmatch.features.petmatch.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petmatch.core.session.UserSession
import com.example.petmatch.features.petmatch.domain.usescases.GetHomesUseCase
import com.example.petmatch.features.petmatch.domain.usescases.GetPetsUseCase
import com.example.petmatch.features.petmatch.presentation.screens.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPetsUseCase: GetPetsUseCase,
    private val getHomesUseCase: GetHomesUseCase,
    private val userSession: UserSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    // Propiedades para que la UI sepa qué rol tiene el usuario
    val isAdmin: Boolean = userSession.isAdmin()
    val isVoluntario: Boolean = userSession.isVoluntario()

    init {
        loadData()
    }

    fun loadData() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val petsResult = getPetsUseCase()
            val homesResult = getHomesUseCase()

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    mascotas = petsResult.getOrDefault(emptyList()),
                    hogares = homesResult.getOrDefault(emptyList()),
                    error = if (petsResult.isFailure) "Error de conexión con el servidor" else null
                )
            }
        }
    }
}