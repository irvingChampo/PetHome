package com.example.petmatch.features.petmatch.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    onNavigateToAssign: (Int, String) -> Unit,
    onNavigateToHealth: (Int, String) -> Unit // <--- Añadir esto
) {
    // OPTIMIZACIÓN: collectAsStateWithLifecycle ahorra batería en segundo plano
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isAdmin = viewModel.isAdmin
    val isVoluntario = viewModel.isVoluntario
    val context = LocalContext.current

    var selectedTab by remember { mutableIntStateOf(if (isAdmin) 0 else 1) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDeleteId by remember { mutableIntStateOf(-1) }

    LaunchedEffect(Unit) { viewModel.loadData() }

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este registro?") },
            confirmButton = {
                TextButton(onClick = {
                    if (selectedTab == 0) formViewModel.deletePet(itemToDeleteId)
                    else formViewModel.deleteHome(itemToDeleteId)
                    showDeleteDialog = false
                    viewModel.loadData()
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PetMatch - ${if (isAdmin) "Administrador" else "Voluntario"}") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            val showFab = (selectedTab == 0 && isAdmin) || (selectedTab == 1 && isVoluntario)
            if (showFab) {
                FloatingActionButton(
                    onClick = { if (selectedTab == 0) onNavigateToAddPet() else onNavigateToAddHome() },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
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
                            PetCard(
                                pet = pet,
                                isAdmin = isAdmin,
                                onEdit = { onNavigateToEditPet(it.id, it.nombre, it.especie, it.edad) },
                                onDelete = { itemToDeleteId = it; showDeleteDialog = true },
                                onAssignClick = onNavigateToAssign,
                                onHealthClick = onNavigateToHealth // <--- Pasar aquí
                            )
                        }
                    } else {
                        items(state.hogares) { home ->
                            HomeCard(
                                home = home,
                                isVoluntario = isVoluntario,
                                onEdit = { onNavigateToEditHome(it.id, it.nombreVoluntario, it.direccion, it.capacidad, it.tipoMascotaAceptada) },
                                onDelete = { itemToDeleteId = it; showDeleteDialog = true }
                            )
                        }
                    }
                }
            }
        }
    }
}