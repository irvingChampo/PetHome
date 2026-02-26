package com.example.petmatch.features.petmatch.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.petmatch.features.petmatch.presentation.viewmodels.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFormScreen(
    viewModel: FormViewModel = hiltViewModel(), // Faltaba esto
    homeId: Int = 0,
    initialName: String = "",
    initialDir: String = "",
    initialCap: String = "",
    initialType: String = "Perros",
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val nombre by viewModel.homeNombre.collectAsState()
    val direccion by viewModel.homeDireccion.collectAsState()
    val capacidad by viewModel.homeCapacidad.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadInitialHomeData(initialName, initialDir, initialCap, initialType)
    }

    if (state.isSuccess) {
        LaunchedEffect(Unit) { viewModel.resetState(); onBack() }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Hogar Temporal", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)) }
    ) { padding ->
        Column(Modifier.padding(padding).padding(24.dp).verticalScroll(rememberScrollState())) {
            OutlinedTextField(value = nombre, onValueChange = { viewModel.updateHomeNombre(it) }, label = { Text("Nombre Voluntario") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = direccion, onValueChange = { viewModel.updateHomeDireccion(it) }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = capacidad, onValueChange = { viewModel.updateHomeCapacidad(it) }, label = { Text("Capacidad") }, modifier = Modifier.fillMaxWidth())

            Button(onClick = { viewModel.saveHome(homeId) }, modifier = Modifier.fillMaxWidth().padding(top = 24.dp), enabled = !state.isLoading) {
                if (state.isLoading) CircularProgressIndicator(Modifier.size(24.dp)) else Text("Guardar")
            }
            TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
        }
    }
}