
package com.example.ars.features.home.domain.model

data class Museo(
    val nombre: String,
    val ciudad: String,
    val obrasDescubiertas: Int,
    val obrasTotales: Int,
    val criaturasDescubiertas: Int,
    val criaturasTotales: Int,
    val id: String,
    val pais: String = "España",
    val destacado: Boolean = false
)
