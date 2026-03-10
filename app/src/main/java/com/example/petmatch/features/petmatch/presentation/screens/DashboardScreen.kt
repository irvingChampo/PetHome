package com.example.petmatch.features.petmatch.presentation.screens

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
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
    onNavigateToEditPet: (Int, String, String, Int, String?) -> Unit,
    onNavigateToEditHome: (Int, String, String, Int, String) -> Unit,
    onNavigateToAssign: (Int, String) -> Unit,
    onNavigateToHealth: (Int, String) -> Unit,      // Feature F03
    onToggleInterest: (Int, Boolean) -> Unit,       // Feature F02
    onNavigateToInterests: (Int, String) -> Unit     // Feature F02
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isAdmin = viewModel.isAdmin
    val isVoluntario = viewModel.isVoluntario
    val context = LocalContext.current

    var selectedTab by remember { mutableIntStateOf(if (isAdmin) 0 else 1) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDeleteId by remember { mutableIntStateOf(-1) }

    LaunchedEffect(Unit) { viewModel.loadData() }

    // Escucha errores generales del Dashboard
    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    // Escucha errores específicos al eliminar (del FormViewModel)
    LaunchedEffect(Unit) {
        formViewModel.errorFlow.collect { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este registro? Se requerirá autenticación.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false

                    showBiometricPrompt(context) {
                        if (selectedTab == 0) {
                            formViewModel.deletePet(itemToDeleteId) {
                                viewModel.loadData()
                            }
                        } else {
                            formViewModel.deleteHome(itemToDeleteId) {
                                viewModel.loadData()
                            }
                        }
                    }
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
                                onEdit = { onNavigateToEditPet(it.id, it.nombre, it.especie, it.edad, it.fotoUrl) },
                                onDelete = { itemToDeleteId = it; showDeleteDialog = true },
                                onAssignClick = onNavigateToAssign,
                                onHealthClick = onNavigateToHealth, // Conectado a F03
                                onToggleInterest = { onToggleInterest(pet.id, pet.isInterested) }, // Conectado a F02
                                onViewInterests = onNavigateToInterests // Conectado a F02
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

// FUNCIÓN AUXILIAR PARA LA BIOMETRÍA ACTUALIZADA CON RESPALDO (PIN/PATRÓN)
private fun showBiometricPrompt(
    context: Context,
    onSuccess: () -> Unit
) {
    val fragmentActivity = context as? FragmentActivity
    if (fragmentActivity == null) {
        Toast.makeText(context, "Error: Dispositivo no compatible con biometría", Toast.LENGTH_SHORT).show()
        return
    }

    val executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(
        fragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Ignoramos el error si el usuario simplemente canceló el diálogo
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    Toast.makeText(context, "Operación cancelada: $errString", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, "Autenticación fallida. Intenta de nuevo.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // ACTUALIZADO: Añadimos soporte para PIN/Patrón si no hay huella
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Autorización Requerida")
        .setSubtitle("Usa tu huella, rostro o el PIN de tu dispositivo para continuar")
        .setAllowedAuthenticators(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        .build()

    biometricPrompt.authenticate(promptInfo)
}