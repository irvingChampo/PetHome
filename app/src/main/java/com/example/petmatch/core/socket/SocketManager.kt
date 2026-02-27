package com.example.petmatch.core.socket

import android.util.Log
import com.example.petmatch.features.petmatch.data.datasources.local.dao.PetMatchDao
import com.example.petmatch.features.petmatch.data.datasources.remote.model.HogarDto
import com.example.petmatch.features.petmatch.data.datasources.remote.model.MascotaDto
import com.example.petmatch.features.petmatch.data.datasources.local.entities.toEntity
import com.example.petmatch.features.petmatch.data.datasources.remote.mapper.toDomain
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
    private val petMatchDao: PetMatchDao
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

            // Eventos de conexión
            mSocket?.on(Socket.EVENT_CONNECT) {
                Log.d("SocketManager", "Conectado al servidor de PetMatch")
            }

            mSocket?.on(Socket.EVENT_DISCONNECT) {
                Log.d("SocketManager", " Desconectado del servidor")
            }

            // --- ESCUCHA DE EVENTOS EN TIEMPO REAL ---

            // 1. Cuando se crea o actualiza una mascota
            mSocket?.on("mascota_actualizada") { args ->
                val data = args[0] as JSONObject
                scope.launch {
                    try {
                        val dto = gson.fromJson(data.toString(), MascotaDto::class.java)
                        petMatchDao.insertPet(dto.toDomain().toEntity())
                        Log.d("SocketManager", " Mascota actualizada vía Socket: ${dto.nombre}")
                    } catch (e: Exception) {
                        Log.e("SocketManager", "Error procesando mascota_actualizada", e)
                    }
                }
            }

            // 2. Cuando se elimina una mascota
            mSocket?.on("mascota_eliminada") { args ->
                val data = args[0] as JSONObject
                val id = data.getInt("id")
                scope.launch {
                    petMatchDao.deletePet(id)
                    Log.d("SocketManager", " Mascota eliminada vía Socket ID: $id")
                }
            }

            // 3. Cuando se crea o actualiza un hogar
            mSocket?.on("hogar_actualizado") { args ->
                val data = args[0] as JSONObject
                scope.launch {
                    try {
                        val dto = gson.fromJson(data.toString(), HogarDto::class.java)
                        petMatchDao.insertHome(dto.toDomain().toEntity())
                        Log.d("SocketManager", " Hogar actualizado vía Socket: ${dto.nombreVoluntario}")
                    } catch (e: Exception) {
                        Log.e("SocketManager", "Error procesando hogar_actualizado", e)
                    }
                }
            }

            // 4. Cuando se elimina un hogar
            mSocket?.on("hogar_eliminado") { args ->
                val data = args[0] as JSONObject
                val id = data.getInt("id")
                scope.launch {
                    petMatchDao.deleteHome(id)
                    Log.d("SocketManager", " Hogar eliminado vía Socket ID: $id")
                }
            }

            mSocket?.connect()

        } catch (e: Exception) {
            Log.e("SocketManager", "Error al configurar Socket.io", e)
        }
    }

    fun disconnect() {
        mSocket?.disconnect()
    }
}