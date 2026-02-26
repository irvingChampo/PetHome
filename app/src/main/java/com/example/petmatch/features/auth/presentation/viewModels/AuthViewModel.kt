package com.example.petmatch.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petmatch.core.session.UserSession
// IMPORTACIONES CORREGIDAS (sin la 's' de más en usecases)
import com.example.petmatch.features.auth.domain.usecases.LoginUseCase
import com.example.petmatch.features.auth.domain.usecases.RegisterUseCase
import com.example.petmatch.features.auth.presentation.screens.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val userSession: UserSession
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _emailLogin = MutableStateFlow("")
    val emailLogin = _emailLogin.asStateFlow()
    private val _passwordLogin = MutableStateFlow("")
    val passwordLogin = _passwordLogin.asStateFlow()

    private val _nombreRegister = MutableStateFlow("")
    val nombreRegister = _nombreRegister.asStateFlow()
    private val _emailRegister = MutableStateFlow("")
    val emailRegister = _emailRegister.asStateFlow()
    private val _passwordRegister = MutableStateFlow("")
    val passwordRegister = _passwordRegister.asStateFlow()
    private val _telefonoRegister = MutableStateFlow("")
    val telefonoRegister = _telefonoRegister.asStateFlow()

    private val _roleRegister = MutableStateFlow("voluntario")
    val roleRegister = _roleRegister.asStateFlow()

    fun updateEmailLogin(email: String) { _emailLogin.value = email }
    fun updatePasswordLogin(password: String) { _passwordLogin.value = password }
    fun updateNombreRegister(nombre: String) { _nombreRegister.value = nombre }
    fun updateEmailRegister(email: String) { _emailRegister.value = email }
    fun updatePasswordRegister(password: String) { _passwordRegister.value = password }
    fun updateTelefonoRegister(telefono: String) { _telefonoRegister.value = telefono }
    fun updateRoleRegister(role: String) { _roleRegister.value = role }

    fun login() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            loginUseCase(_emailLogin.value, _passwordLogin.value).fold(
                onSuccess = { user ->
                    userSession.saveSession(user.token, user.role)
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { _uiState.update { it.copy(isLoading = false, error = "Credenciales inválidas") } }
            )
        }
    }

    fun register() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            // Se pasan los 5 parámetros requeridos
            registerUseCase(
                _nombreRegister.value,
                _emailRegister.value,
                _passwordRegister.value,
                _telefonoRegister.value,
                _roleRegister.value
            ).fold(
                onSuccess = { user ->
                    userSession.saveSession(user.token, user.role)
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { err ->
                    _uiState.update { it.copy(isLoading = false, error = err.message ?: "Error desconocido") }
                }
            )
        }
    }
}