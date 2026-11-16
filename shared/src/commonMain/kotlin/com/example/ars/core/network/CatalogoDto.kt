package com.example.ars.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Filas de las tablas del catálogo tal y como llegan de Supabase.
// Cada repositorio las convierte a sus propios modelos de domain.

@Serializable
data class MuseoDto(
    val id: String,
    val nombre: String,
    val ciudad: String,
    val pais: String,
    val descripcion: String = "",
    val destacado: Boolean = false,
    @SerialName("criatura_bienvenida_id") val criaturaBienvenidaId: String? = null,
    @SerialName("recompensa_misiones_id") val recompensaMisionesId: String? = null
)

@Serializable
data class CriaturaDto(
    val id: String,
    val nombre: String,
    val descripcion: String = "",
    @SerialName("museo_id") val museoId: String? = null
)

@Serializable
data class ObraDto(
    val id: String,
    val titulo: String,
    val autor: String,
    @SerialName("museo_id") val museoId: String,
    val descripcion: String = ""
)

@Serializable
data class MisionDto(
    val id: String,
    @SerialName("museo_id") val museoId: String,
    val titulo: String,
    val tipo: String,
    @SerialName("cantidad_obras") val cantidadObras: Int? = null,
    @SerialName("recompensa_criatura_id") val recompensaCriaturaId: String? = null,
    val orden: Int = 1,
    // obras que pide una misión de tipo OBRAS_CONCRETAS (tabla mision_obra)
    @SerialName("mision_obra") val obras: List<MisionObraDto> = emptyList()
)

@Serializable
data class MisionObraDto(
    @SerialName("obra_id") val obraId: String
)
