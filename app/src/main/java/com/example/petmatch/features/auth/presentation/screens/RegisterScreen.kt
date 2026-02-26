package com.example.petmatch.features.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.petmatch.features.auth.presentation.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val nombre by viewModel.nombreRegister.collectAsState()
    val email by viewModel.emailRegister.collectAsState()
    val password by viewModel.passwordRegister.collectAsState()
    val telefono by viewModel.telefonoRegister.collectAsState()
    val roleSelected by viewModel.roleRegister.collectAsState()
    val context = LocalContext.current

    if (state.isSuccess) {
        LaunchedEffect(Unit) { onRegisterSuccess() }
    }

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = nombre, onValueChange = { viewModel.updateNombreRegister(it) }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { viewModel.updateEmailRegister(it) }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { viewModel.updatePasswordRegister(it) }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = telefono, onValueChange = { viewModel.updateTelefonoRegister(it) }, label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Text("Selecciona tu rol:", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            RadioButton(selected = roleSelected == "voluntario", onClick = { viewModel.updateRoleRegister("voluntario") })
            Text("Voluntario")
            Spacer(Modifier.width(16.dp))
            RadioButton(selected = roleSelected == "admin", onClick = { viewModel.updateRoleRegister("admin") })
            Text("Administrador")
        }

        Button(
            onClick = { viewModel.register() },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) CircularProgressIndicator(Modifier.size(24.dp)) else Text("Registrarme")
        }

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}