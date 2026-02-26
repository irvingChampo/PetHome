package com.example.petmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.petmatch.core.navigation.AppNavigation
import com.example.petmatch.ui.theme.PetMatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetMatchTheme {
                // Ya no pasamos parámetros, Hilt se encarga dentro de la navegación
                AppNavigation()
            }
        }
    }
}