
package com.example.ars.features.museos.domain.model

data class Museo(
    val id: String,
    val nombre: String,
    val ciudad: String,
    val pais: String,
    val obrasDescubiertas: Int,
    val obrasTotales: Int,
    val criaturasDescubiertas: Int,
    val criaturasTotales: Int,
    val destacado: Boolean = false,
    val descripcion: String = "",
    val misiones: List<String> = emptyList(),
    val criaturasIds: List<String> = emptyList(),
    val criaturaBienvenidaId: String? = null
)
