package com.example.petmatch.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PetMatchPrimary,
    secondary = PetMatchSecondary,
    background = PetMatchBackground,
    surface = PetMatchWhite,
    error = PetMatchError
)

@Composable
fun PetMatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}