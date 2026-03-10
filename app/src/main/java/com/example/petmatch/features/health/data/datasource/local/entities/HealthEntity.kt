package com.example.petmatch.features.health.data.datasources.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.petmatch.features.health.domain.entities.Health
import com.example.petmatch.features.petmatch.data.datasources.local.entities.PetEntity

@Entity(
    tableName = "pet_health_history",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["mascotaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["mascotaId"])]
)
data class HealthEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mascotaId: Int,
    val diagnostico: String,
    val vacuna: String?,
    val fechaTratamiento: String
)

// Mappers para separar Capa de Datos de Capa de Dominio
fun HealthEntity.toDomain() = Health(
    id = this.id,
    mascotaId = this.mascotaId,
    diagnostico = this.diagnostico,
    vacuna = this.vacuna,
    fechaTratamiento = this.fechaTratamiento
)

fun Health.toEntity() = HealthEntity(
    id = if (this.id == 0) 0 else this.id, // 0 permite que Room genere el ID auto-incremental
    mascotaId = this.mascotaId,
    diagnostico = this.diagnostico,
    vacuna = this.vacuna,
    fechaTratamiento = this.fechaTratamiento
)