package com.example.petmatch.features.interest.presentation.screens

import com.example.petmatch.features.interest.domain.entities.Interest

data class InterestUiState(
    val isLoading: Boolean = false,
    val interests: List<Interest> = emptyList()
)