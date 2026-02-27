package com.example.petmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.petmatch.core.navigation.AppNavigation
import com.example.petmatch.core.socket.PetMatchSocketManager // Añadir import
import com.example.petmatch.ui.theme.PetMatchTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject // Añadir import

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var socketManager: PetMatchSocketManager // Inyectar manager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Iniciar la conexión en tiempo real nada más abrir la app
        socketManager.connect()

        enableEdgeToEdge()
        setContent {
            PetMatchTheme {
                AppNavigation()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Desconectar para ahorrar batería y recursos al cerrar la app
        socketManager.disconnect()
    }
}