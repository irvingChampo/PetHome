package com.example.petmatch.features.petmatch.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.petmatch.features.petmatch.presentation.components.HomeCard
import com.example.petmatch.features.petmatch.presentation.components.PetCard
import com.example.petmatch.features.petmatch.presentation.viewmodels.DashboardViewModel
import com.example.petmatch.features.petmatch.presentation.viewmodels.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    formViewModel: FormViewModel = hiltViewModel(),
    onNavigateToAddPet: () -> Unit,
    onNavigateToAddHome: () -> Unit,
    onNavigateToEditPet: (Int, String, String, Int) -> Unit,
    onNavigateToEditHome: (Int, String, String, Int, String) -> Unit,
    onNavigateToAssign: (Int, String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDeleteId by remember { mutableIntStateOf(-1) }

    LaunchedEffect(Unit) { viewModel.loadData() }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar") },
            text = { Text("¿Deseas eliminar este registro?") },
            confirmButton = {
                TextButton(onClick = {
                    if (selectedTab == 0) formViewModel.deletePet(itemToDeleteId)
                    else formViewModel.deleteHome(itemToDeleteId)
                    showDeleteDialog = false
                    viewModel.loadData()
                }) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PetMatch", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { if (selectedTab == 0) onNavigateToAddPet() else onNavigateToAddHome() }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Mascotas") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Hogares") })
            }

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            } else {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)) {
                    if (selectedTab == 0) {
                        items(state.mascotas) { pet ->
                            PetCard(pet, onEdit = { onNavigateToEditPet(it.id, it.nombre, it.especie, it.edad) },
                                onDelete = { itemToDeleteId = it; showDeleteDialog = true }, onAssignClick = onNavigateToAssign)
                        }
                    } else {
                        items(state.hogares) { home ->
                            HomeCard(home, onEdit = { onNavigateToEditHome(it.id, it.nombreVoluntario, it.direccion, it.capacidad, it.tipoMascotaAceptada) },
                                onDelete = { itemToDeleteId = it; showDeleteDialog = true })
                        }
                    }
                }
            }
        }
    }
}