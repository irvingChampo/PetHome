package com.example.petmatch.core.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager


fun triggerErrorVibration(context: Context) {
    // Obtenemos el servicio de vibración dependiendo de la versión de Android
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (!vibrator.hasVibrator()) return

    // Patrón: Espera 0ms, vibra 50ms, espera 50ms, vibra 50ms
    val timings = longArrayOf(0, 150, 50, 150)
    // Amplitud: 0 (apagado), 255 (máxima fuerza)
    val amplitudes = intArrayOf(0, 255, 0, 255)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
        vibrator.vibrate(effect)
    } else {
        // Para versiones de Android más antiguas
        @Suppress("DEPRECATION")
        vibrator.vibrate(timings, -1)
    }
}