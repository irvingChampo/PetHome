package com.example.petmatch.features.petmatch.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.petmatch.features.petmatch.presentation.viewmodels.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFormScreen(
    viewModel: FormViewModel,
    homeId: Int = 0,
    initialName: String = "",
    initialDir: String = "",
    initialCap: String = "",
    initialType: String = "Perros",
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Obtenemos los valores desde el ViewModel (Hot Flows)
    val nombre by viewModel.homeNombre.collectAsState()
    val direccion by viewModel.homeDireccion.collectAsState()
    val telefono by viewModel.homeTelefono.collectAsState()
    val capacidad by viewModel.homeCapacidad.collectAsState()
    val tipoSeleccionado by viewModel.homeTipo.collectAsState()

    val opcionesTipo = listOf("Perros", "Gatos", "Ambos")
    var expanded by remember { mutableStateOf(false) }

    var isInitialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isInitialized) {
            viewModel.loadInitialHomeData(initialName, initialDir, initialCap, initialType)
            isInitialized = true
        }
    }

    if (state.isSuccess) {
        LaunchedEffect(Unit) {
            viewModel.resetState()
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (homeId == 0) "Registrar Hogar" else "Editar Hogar", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { viewModel.updateHomeNombre(it) },
                label = { Text("Nombre del Voluntario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = { viewModel.updateHomeDireccion(it) },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.updateHomeTelefono(it) },
                label = { Text("Confirmar Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = capacidad,
                onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.updateHomeCapacidad(it) },
                label = { Text("Capacidad Máxima") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            ExposedDropdownMenuBox(expanded, { expanded = !expanded }, Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = tipoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Mascota Aceptada") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded, { expanded = false }) {
                    opcionesTipo.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                viewModel.updateHomeTipo(tipo)
                                expanded = false
                            }
                        )
                    }
                }
            }

            if (state.error != null) {
                Text(state.error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { viewModel.saveHome(homeId) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) CircularProgressIndicator(Modifier.size(24.dp), color = Color.White)
                else Text(if (homeId == 0) "Guardar Hogar" else "Actualizar Hogar")
            }

            TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Cancelar")
            }
        }
    }
}