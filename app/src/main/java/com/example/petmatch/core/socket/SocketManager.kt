package com.example.petmatch.core.socket

import android.util.Log
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import com.example.petmatch.features.health.data.datasources.local.dao.HealthDao
import com.example.petmatch.features.interest.data.datasources.local.dao.InterestDao // Nuevo import
import com.example.petmatch.features.petmatch.data.datasources.remote.model.HogarDto
import com.example.petmatch.features.petmatch.data.datasources.remote.model.MascotaDto
import com.example.petmatch.features.health.data.datasources.remote.model.HealthDto
import com.example.petmatch.features.interest.data.datasources.remote.model.InterestDto // Nuevo import
import com.example.petmatch.features.petmatch.data.datasources.local.entities.toEntity
import com.example.petmatch.features.health.data.datasources.local.entities.toEntity
import com.example.petmatch.features.interest.data.datasources.local.entities.toEntity // Nuevo import
import com.example.petmatch.features.petmatch.data.datasources.remote.mapper.toDomain
import com.example.petmatch.features.health.data.datasources.remote.mapper.toDomain
import com.example.petmatch.features.interest.data.datasources.remote.mapper.toDomain // Nuevo import
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
    private val healthDao: HealthDao,
    private val interestDao: InterestDao // Inyectado
) {
    private var mSocket: Socket? = null
    private val gson = Gson()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun connect() {
        try {
            val opts = IO.Options()
            opts.forceNew = true
            opts.reconnection = true

            mSocket = IO.socket("https://backend-petmatch-api.onrender.com", opts)

            mSocket?.on(Socket.EVENT_CONNECT) { Log.d("SocketManager", "Conectado") }
            mSocket?.on(Socket.EVENT_DISCONNECT) { Log.d("SocketManager", "Desconectado") }

            // ... EVENTOS DE MASCOTAS, HOGARES Y SALUD (Anteriores) ...
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
            mSocket?.on("nuevo_registro_salud") { args ->
                val data = args[0] as JSONObject
                scope.launch {
                    val dto = gson.fromJson(data.toString(), HealthDto::class.java)
                    healthDao.insertHealthRecord(dto.toDomain().toEntity())
                }
            }

            // --- NUEVO: EVENTO DE INTERÉS (FEATURE F02) ---
            mSocket?.on("nuevo_interes") { args ->
                val data = args[0] as JSONObject
                scope.launch {
                    try {
                        val dto = gson.fromJson(data.toString(), InterestDto::class.java)

                        // 1. Guardar el interés para el panel del Admin
                        interestDao.insertInterest(dto.toDomain().toEntity())

                        // 2. Actualizar visualmente el corazón en la lista de mascotas global
                        interestDao.updatePetInterestStatus(dto.mascotaId, true)

                        Log.d("SocketManager", "Nuevo interés recibido vía Socket: Mascota ${dto.mascotaId}")
                    } catch (e: Exception) {
                        Log.e("SocketManager", "Error procesando nuevo_interes", e)
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