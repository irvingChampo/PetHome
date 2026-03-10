package com.example.petmatch.features.health.presentation.screens

import com.example.petmatch.features.health.domain.entities.Health

data class HealthUiState(
    val isLoading: Boolean = false,
    val history: List<Health> = emptyList(),
    val isSuccess: Boolean = false
)