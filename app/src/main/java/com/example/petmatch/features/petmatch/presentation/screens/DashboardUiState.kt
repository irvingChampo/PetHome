package com.example.petmatch.features.petmatch.presentation.screens

import com.example.petmatch.features.petmatch.domain.entities.Pet
import com.example.petmatch.features.petmatch.domain.entities.Home

data class DashboardUiState(
    val isLoading: Boolean = false,
    val mascotas: List<Pet> = emptyList(),
    val hogares: List<Home> = emptyList(),
    val error: String? = null
)