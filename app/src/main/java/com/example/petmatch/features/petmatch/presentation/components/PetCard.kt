package com.example.petmatch.features.petmatch.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petmatch.features.petmatch.domain.entities.Pet

@Composable
fun PetCard(
    pet: Pet,
    isAdmin: Boolean,
    onEdit: (Pet) -> Unit,
    onDelete: (Int) -> Unit,
    onAssignClick: (Int, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(pet.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${pet.especie} • ${pet.edad} años", style = MaterialTheme.typography.bodyMedium)
                }

                if (isAdmin) {
                    Row {
                        IconButton(onClick = { onEdit(pet) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { onDelete(pet.id) }) {
                            // Uso de categoría ERROR del tema
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Badge de estado usando colores del tema
            Surface(
                color = if (pet.estado == "Sin hogar") MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = pet.estado,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = if (pet.estado == "Sin hogar") MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            if (isAdmin && pet.estado == "Sin hogar") {
                Button(
                    onClick = { onAssignClick(pet.id, pet.nombre) },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Asignar Hogar")
                }
            }
        }
    }
}