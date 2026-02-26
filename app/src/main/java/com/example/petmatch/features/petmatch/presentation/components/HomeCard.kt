package com.example.petmatch.features.petmatch.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.petmatch.features.petmatch.domain.entities.Home

@Composable
fun HomeCard(
    home: Home,
    isVoluntario: Boolean, // Nuevo parámetro para control de rol
    onEdit: (Home) -> Unit,
    onDelete: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text(home.nombreVoluntario, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))

                // SOLO EL VOLUNTARIO PUEDE EDITAR O ELIMINAR HOGARES
                if (isVoluntario) {
                    Row {
                        IconButton(onClick = { onEdit(home) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { onDelete(home.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFE63946))
                        }
                    }
                }
            }
            Text(home.direccion)
            Text("Capacidad: ${home.ocupacionActual} / ${home.capacidad}")
            Text("Acepta: ${home.tipoMascotaAceptada}")
        }
    }
}