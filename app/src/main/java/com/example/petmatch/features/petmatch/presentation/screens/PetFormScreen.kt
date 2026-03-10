package com.example.petmatch.features.petmatch.presentation.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.petmatch.features.petmatch.presentation.viewmodels.FormViewModel
import com.example.petmatch.core.utils.triggerErrorVibration // NUEVO IMPORT
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFormScreen(
    viewModel: FormViewModel = hiltViewModel(),
    petId: Int = 0,
    initialName: String = "",
    initialSpecie: String = "Perro",
    initialAge: String = "",
    initialFotoUrl: String? = null,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val nombre by viewModel.petNombre.collectAsState()
    val especie by viewModel.petEspecie.collectAsState()
    val edad by viewModel.petEdad.collectAsState()

    // ACTUALIZADO: Observamos la RUTA física del archivo
    val imagePath by viewModel.petImagePath.collectAsState()

    val context = LocalContext.current

    // Estado para guardar la información del archivo mientras tomamos la foto
    var pendingPhotoFile by remember { mutableStateOf<File?>(null) }
    var pendingPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzador de la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && pendingPhotoFile != null) {
                // ACTUALIZADO: Guardamos la ruta absoluta del archivo
                viewModel.updatePetImagePath(pendingPhotoFile!!.absolutePath)
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.loadInitialPetData(initialName, initialSpecie, initialAge, initialFotoUrl)
    }

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { errorMessage ->
            // NUEVO: Hacemos vibrar el teléfono antes de mostrar el mensaje de error del formulario
            triggerErrorVibration(context)
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    if (state.isSuccess) {
        LaunchedEffect(Unit) { viewModel.resetState(); onBack() }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mascota", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // SECCIÓN DE LA CÁMARA
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        // ACTUALIZADO: Obtenemos el par (URI temporal, Archivo físico)
                        val photoData = createTempImageFile(context)
                        pendingPhotoUri = photoData.first
                        pendingPhotoFile = photoData.second

                        // Lanzamos la cámara pasándole la URI temporal
                        cameraLauncher.launch(pendingPhotoUri!!)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imagePath != null) {
                    // ACTUALIZADO: Coil sabe cómo cargar archivos si le damos el objeto File
                    AsyncImage(
                        model = File(imagePath!!),
                        contentDescription = "Foto de la mascota",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Tomar Foto",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tomar Foto", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            // FIN SECCIÓN DE LA CÁMARA

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(value = nombre, onValueChange = { viewModel.updatePetNombre(it) }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = edad, onValueChange = { viewModel.updatePetEdad(it) }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())

            Button(onClick = { viewModel.savePet(petId) }, modifier = Modifier.fillMaxWidth().padding(top = 32.dp), enabled = !state.isLoading) {
                if (state.isLoading) CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary) else Text("Guardar")
            }
        }
    }
}

// ACTUALIZADO: Ahora devuelve un Pair con la URI (para la cámara) y el File (para guardar la ruta)
private fun createTempImageFile(context: Context): Pair<Uri, File> {
    val directory = File(context.cacheDir, "camera_photos")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val file = File.createTempFile("PET_", ".jpg", directory)
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    return Pair(uri, file)
}