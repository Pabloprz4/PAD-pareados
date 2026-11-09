package com.example.ars.features.home.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProgresoUsuario(
    val criaturaInicialId: String? = null,
    val criaturasDescubiertas: List<String> = emptyList(),
    val museosVisitados: List<String> = emptyList()
)