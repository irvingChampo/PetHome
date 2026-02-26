package com.example.petmatch.features.petmatch.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.petmatch.features.petmatch.presentation.viewmodels.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFormScreen(
    viewModel: FormViewModel = hiltViewModel(),
    petId: Int = 0,
    initialName: String = "",
    initialSpecie: String = "Perro",
    initialAge: String = "",
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val nombre by viewModel.petNombre.collectAsState()
    val especie by viewModel.petEspecie.collectAsState()
    val edad by viewModel.petEdad.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadInitialPetData(initialName, initialSpecie, initialAge)
    }

    if (state.isSuccess) {
        LaunchedEffect(Unit) { viewModel.resetState(); onBack() }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mascota", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)) }
    ) { padding ->
        Column(Modifier.padding(padding).padding(24.dp)) {
            OutlinedTextField(value = nombre, onValueChange = { viewModel.updatePetNombre(it) }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = edad, onValueChange = { viewModel.updatePetEdad(it) }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = { viewModel.savePet(petId) }, modifier = Modifier.fillMaxWidth().padding(top = 32.dp), enabled = !state.isLoading) {
                if (state.isLoading) CircularProgressIndicator(Modifier.size(24.dp)) else Text("Guardar")
            }
        }
    }
}