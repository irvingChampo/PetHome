package com.example.petmatch.features.petmatch.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.petmatch.features.petmatch.domain.entities.Pet

@Composable
fun PetCard(
    pet: Pet,
    isAdmin: Boolean,
    onEdit: (Pet) -> Unit,
    onDelete: (Int) -> Unit,
    onAssignClick: (Int, String) -> Unit,
    onHealthClick: (Int, String) -> Unit,
    onToggleInterest: () -> Unit, // Nuevo: Clic en el corazón
    onViewInterests: (Int, String) -> Unit // Nuevo: Solo Admin ve quiénes están interesados
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {

                // --- NUEVA SECCIÓN DE IMAGEN ---
                if (!pet.fotoUrl.isNullOrEmpty()) {
                    // Verificamos si es una URL de internet o una ruta local
                    val imageModel = if (pet.fotoUrl.startsWith("http")) {
                        pet.fotoUrl // Es de internet
                    } else {
                        java.io.File(pet.fotoUrl) // Es de la cámara
                    }

                    AsyncImage(
                        model = imageModel,
                        contentDescription = "Foto de ${pet.nombre}",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder si no tiene foto
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pets,
                            contentDescription = "Sin foto",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))
                // --- FIN SECCIÓN IMAGEN ---

                Column(Modifier.weight(1f)) {
                    Text(pet.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${pet.especie} • ${pet.edad} años", style = MaterialTheme.typography.bodyMedium)
                }

                Row {
                    // BOTÓN DE CORAZÓN (Favoritos/Interés) - Visible para todos
                    IconButton(onClick = onToggleInterest) {
                        Icon(
                            imageVector = if (pet.isInterested) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Interés",
                            tint = if (pet.isInterested) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Botón de Historial Médico
                    IconButton(onClick = { onHealthClick(pet.id, pet.nombre) }) {
                        Icon(Icons.Default.MedicalServices, null, tint = MaterialTheme.colorScheme.secondary)
                    }

                    // Botón Ver Interesados (Solo Admin)
                    if (isAdmin) {
                        IconButton(onClick = { onViewInterests(pet.id, pet.nombre) }) {
                            Icon(Icons.Default.Groups, contentDescription = "Ver Interesados", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { onEdit(pet) }) {
                            Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { onDelete(pet.id) }) {
                            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

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