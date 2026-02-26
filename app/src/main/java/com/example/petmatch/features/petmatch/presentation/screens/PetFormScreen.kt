package com.example.petmatch.features.petmatch.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.petmatch.features.petmatch.presentation.viewmodels.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFormScreen(
    viewModel: FormViewModel,
    petId: Int = 0,
    initialName: String = "",
    initialSpecie: String = "Perro",
    initialAge: String = "",
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()


    val nombre by viewModel.petNombre.collectAsState()
    val especieSeleccionada by viewModel.petEspecie.collectAsState()
    val edad by viewModel.petEdad.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var isInitialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isInitialized) {
            viewModel.loadInitialPetData(initialName, initialSpecie, initialAge)
            isInitialized = true
        }
    }

    if (state.isSuccess) {
        LaunchedEffect(Unit) { viewModel.resetState(); onBack() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (petId == 0) "Nueva Mascota" else "Editar Mascota", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(24.dp)) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { viewModel.updatePetNombre(it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            ExposedDropdownMenuBox(expanded, { expanded = !expanded }, Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = especieSeleccionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Especie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded, { expanded = false }) {
                    listOf("Perro", "Gato", "Otro").forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                viewModel.updatePetEspecie(opcion)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = edad,
                onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.updatePetEdad(it) },
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth()
            )

            if (state.error != null) {
                Text(state.error!!, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Button(
                onClick = { viewModel.savePet(petId) },
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) CircularProgressIndicator(Modifier.size(24.dp), color = Color.White)
                else Text(if (petId == 0) "Guardar" else "Actualizar")
            }
        }
    }
}