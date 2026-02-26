package com.example.petmatch.features.petmatch.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petmatch.features.petmatch.presentation.viewmodels.DashboardViewModel
import com.example.petmatch.features.petmatch.presentation.viewmodels.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignPetScreen(
    petId: Int,
    petName: String,
    formViewModel: FormViewModel,
    dashboardViewModel: DashboardViewModel,
    onBack: () -> Unit
) {
    val formState by formViewModel.uiState.collectAsState()
    val dashState by dashboardViewModel.uiState.collectAsState()

    // Cargar hogares al iniciar
    LaunchedEffect(Unit) {
        dashboardViewModel.loadData()
    }

    if (formState.isSuccess) {
        LaunchedEffect(Unit) {
            formViewModel.resetState()
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asignar Hogar a $petName", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            Text(
                "Selecciona un hogar disponible:",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )

            if (dashState.isLoading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            } else {
                LazyColumn(Modifier.weight(1f)) {
                    // Solo mostrar hogares con capacidad disponible
                    val hogaresDisponibles = dashState.hogares.filter { it.ocupacionActual < it.capacidad }

                    items(hogaresDisponibles) { hogar ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    formViewModel.assignPetToHome(petId, hogar.id, hogar.ocupacionActual)
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(hogar.nombreVoluntario, fontWeight = FontWeight.Bold)
                                    Text(hogar.direccion, style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        "Ocupación: ${hogar.ocupacionActual}/${hogar.capacidad}",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                if (formState.isLoading) {
                                    CircularProgressIndicator(Modifier.size(24.dp))
                                } else {
                                    Text("Seleccionar >", color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                        }
                    }
                }
            }

            formState.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancelar")
            }
        }
    }
}