package com.example.petmatch.features.health.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petmatch.features.health.presentation.viewmodels.PetHealthViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetHealthScreen(
    petId: Int,
    petName: String,
    viewModel: PetHealthViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val diagnostico by viewModel.diagnostico.collectAsStateWithLifecycle()
    val vacuna by viewModel.vacuna.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }

    // Cargar datos al iniciar
    LaunchedEffect(petId) { viewModel.loadHistory(petId) }

    // Manejo de errores
    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    // Cerrar diálogo si se guardó con éxito
    if (state.isSuccess) {
        LaunchedEffect(Unit) {
            showAddDialog = false
            viewModel.resetSuccess()
            Toast.makeText(context, "Registro guardado", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial: $petName") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            if (viewModel.isAdmin) {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, "Nuevo Registro")
                }
            }
        }
    ) { padding ->
        if (state.isLoading && state.history.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) { CircularProgressIndicator() }
        } else if (state.history.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                Text("No hay registros médicos aún", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
                items(state.history) { record ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MedicalServices, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(record.fechaTratamiento, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("Diagnóstico:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(record.diagnostico, style = MaterialTheme.typography.bodyLarge)

                            if (!record.vacuna.isNullOrBlank()) {
                                Spacer(Modifier.height(8.dp))
                                Text("Vacuna aplicada:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                AssistChip(onClick = {}, label = { Text(record.vacuna) })
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para agregar registro (Solo Admin)
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Nuevo Registro Médico") },
            text = {
                Column {
                    OutlinedTextField(
                        value = diagnostico,
                        onValueChange = { viewModel.updateDiagnostico(it) },
                        label = { Text("Diagnóstico / Observación") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = vacuna,
                        onValueChange = { viewModel.updateVacuna(it) },
                        label = { Text("Vacuna (Opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.saveRecord(petId, LocalDate.now().toString())
                }) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { showAddDialog = false }) { Text("Cancelar") } }
        )
    }
}