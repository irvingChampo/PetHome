package com.example.petmatch.features.interest.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petmatch.features.interest.presentation.viewmodels.InterestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetInterestsScreen(
    petId: Int,
    petName: String,
    viewModel: InterestViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(petId) { viewModel.loadInterests(petId) }

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Interesados en $petName") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") }
                }
            )
        }
    ) { padding ->
        if (state.isLoading && state.interests.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) { CircularProgressIndicator() }
        } else if (state.interests.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                Text("Nadie ha expresado interés aún", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                items(state.interests) { interest ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(interest.nombreUsuario ?: "Usuario desconocido", fontWeight = FontWeight.Bold)
                                Text(interest.emailUsuario ?: "", style = MaterialTheme.typography.bodySmall)
                                interest.telefonoUsuario?.let {
                                    Text("Tel: $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}