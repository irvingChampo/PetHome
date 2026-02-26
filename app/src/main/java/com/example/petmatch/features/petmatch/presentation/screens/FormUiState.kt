package com.example.petmatch.features.petmatch.presentation.screens

data class FormUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)