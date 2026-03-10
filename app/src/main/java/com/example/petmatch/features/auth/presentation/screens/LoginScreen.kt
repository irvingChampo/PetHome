package com.example.petmatch.features.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petmatch.features.auth.presentation.viewmodels.AuthViewModel
import com.example.petmatch.core.utils.triggerErrorVibration

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // OPTIMIZACIÓN: collectAsStateWithLifecycle
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val email by viewModel.emailLogin.collectAsStateWithLifecycle()
    val pass by viewModel.passwordLogin.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (state.isSuccess) {
        LaunchedEffect(Unit) { onLoginSuccess() }
    }

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { errorMessage ->
            // NUEVO: Hacemos vibrar el teléfono antes de mostrar el mensaje
            triggerErrorVibration(context)
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Text(
            text = "PetMatch",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.updateEmailLogin(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = pass,
            onValueChange = { viewModel.updatePasswordLogin(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { viewModel.login() },
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            else Text("Iniciar Sesión")
        }
        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}