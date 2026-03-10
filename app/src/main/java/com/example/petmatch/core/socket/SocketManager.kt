package com.example.petmatch.core.socket

import android.util.Log
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import com.example.petmatch.features.health.data.datasources.local.dao.HealthDao // Nuevo import
import com.example.petmatch.features.petmatch.data.datasources.remote.model.HogarDto
import com.example.petmatch.features.petmatch.data.datasources.remote.model.MascotaDto
import com.example.petmatch.features.health.data.datasources.remote.model.HealthDto // Nuevo import
import com.example.petmatch.features.petmatch.data.datasources.local.entities.toEntity
import com.example.petmatch.features.health.data.datasources.local.entities.toEntity // Nuevo import
import com.example.petmatch.features.petmatch.data.datasources.remote.mapper.toDomain
import com.example.petmatch.features.health.data.datasources.remote.mapper.toDomain // Nuevo import
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetMatchSocketManager @Inject constructor(
    private val petMatchDao: PetMatchDao,
    private val healthDao: HealthDao // Inyectado
) {
    private var mSocket: Socket? = null
    private val gson = Gson()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun connect() {
        try {
            val opts = IO.Options()
            opts.forceNew = true
            opts.reconnection = true

            // URL del servidor (asegúrate que coincida con tu backend)
            mSocket = IO.socket("https://backend-petmatch-api.onrender.com", opts)

            mSocket?.on(Socket.EVENT_CONNECT) { Log.d("SocketManager", "Conectado") }
            mSocket?.on(Socket.EVENT_DISCONNECT) { Log.d("SocketManager", "Desconectado") }

            // --- EVENTOS DE MASCOTAS Y HOGARES (Existentes) ---
            mSocket?.on("mascota_actualizada") { args ->
                val data = args[0] as JSONObject
                scope.launch {
                    val dto = gson.fromJson(data.toString(), MascotaDto::class.java)
                    petMatchDao.insertPet(dto.toDomain().toEntity())
                }
            }

            mSocket?.on("mascota_eliminada") { args ->
                val data = args[0] as JSONObject
                scope.launch { petMatchDao.deletePet(data.getInt("id")) }
            }

            mSocket?.on("hogar_actualizado") { args ->
                val data = args[0] as JSONObject
                scope.launch {
                    val dto = gson.fromJson(data.toString(), HogarDto::class.java)
                    petMatchDao.insertHome(dto.toDomain().toEntity())
                }
            }

            mSocket?.on("hogar_eliminado") { args ->
                val data = args[0] as JSONObject
                scope.launch { petMatchDao.deleteHome(data.getInt("id")) }
            }

            // --- NUEVO: EVENTO DE SALUD (FEATURE F03) ---
            mSocket?.on("nuevo_registro_salud") { args ->
                val data = args[0] as JSONObject
                scope.launch {
                    try {
                        val dto = gson.fromJson(data.toString(), HealthDto::class.java)
                        // Insertamos en Room de la feature health
                        healthDao.insertHealthRecord(dto.toDomain().toEntity())
                        Log.d("SocketManager", "Nuevo registro de salud recibido vía Socket para mascota: ${dto.mascotaId}")
                    } catch (e: Exception) {
                        Log.e("SocketManager", "Error procesando nuevo_registro_salud", e)
                    }
                }
            }

            mSocket?.connect()
        } catch (e: Exception) {
            Log.e("SocketManager", "Error Socket.io", e)
        }
    }

    fun disconnect() {
        mSocket?.disconnect()
    }
}