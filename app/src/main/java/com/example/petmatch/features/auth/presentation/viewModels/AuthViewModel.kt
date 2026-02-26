package com.example.petmatch.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petmatch.features.auth.domain.usecases.*
import com.example.petmatch.features.auth.presentation.screens.AuthUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _emailLogin = MutableStateFlow("")
    val emailLogin = _emailLogin.asStateFlow()

    private val _passwordLogin = MutableStateFlow("")
    val passwordLogin = _passwordLogin.asStateFlow()

    fun updateEmailLogin(email: String) { _emailLogin.value = email }
    fun updatePasswordLogin(password: String) { _passwordLogin.value = password }

    fun login() {
        val currentEmail = _emailLogin.value
        val currentPass = _passwordLogin.value


        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        if (!emailRegex.matches(currentEmail)) {
            _uiState.update { it.copy(error = "Por favor, ingresa un correo electrónico válido") }
            return
        }

        if (currentPass.isBlank()) {
            _uiState.update { it.copy(error = "La contraseña no puede estar vacía") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            loginUseCase(currentEmail, currentPass).fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isSuccess = true) } },
                onFailure = { err -> _uiState.update { it.copy(isLoading = false, error = "Error: Credenciales inválidas") } }
            )
        }
    }


    private val _nombreRegister = MutableStateFlow("")
    val nombreRegister = _nombreRegister.asStateFlow()

    private val _emailRegister = MutableStateFlow("")
    val emailRegister = _emailRegister.asStateFlow()

    private val _passwordRegister = MutableStateFlow("")
    val passwordRegister = _passwordRegister.asStateFlow()

    private val _telefonoRegister = MutableStateFlow("")
    val telefonoRegister = _telefonoRegister.asStateFlow()


    fun updateNombreRegister(nombre: String) { _nombreRegister.value = nombre }
    fun updateEmailRegister(email: String) { _emailRegister.value = email }
    fun updatePasswordRegister(password: String) { _passwordRegister.value = password }
    fun updateTelefonoRegister(telefono: String) { _telefonoRegister.value = telefono }


    fun register() {
        val currentNombre = _nombreRegister.value
        val currentEmail = _emailRegister.value
        val currentPass = _passwordRegister.value
        val currentTel = _telefonoRegister.value


        if (currentNombre.isBlank() || currentPass.isBlank() || currentTel.isBlank()) {
            _uiState.update { it.copy(error = "Por favor, llena todos los campos") }
            return
        }


        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        if (!emailRegex.matches(currentEmail)) {
            _uiState.update { it.copy(error = "Por favor, ingresa un correo electrónico válido") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            registerUseCase(currentNombre, currentEmail, currentPass, currentTel).fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isSuccess = true) } },
                onFailure = { err -> _uiState.update { it.copy(isLoading = false, error = err.message) } }
            )
        }
    }
}