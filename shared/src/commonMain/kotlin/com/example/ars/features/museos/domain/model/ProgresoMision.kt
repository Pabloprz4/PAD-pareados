package com.example.ars.features.museos.domain.model

import com.example.ars.features.home.domain.model.ProgresoUsuario

data class ProgresoMision(
    val mision: Mision,
    val actual: Int,
    val total: Int,
    val completada: Boolean,
    val disponible: Boolean
)

data class EvaluacionMisiones(
    val progreso: ProgresoUsuario,
    val misiones: List<ProgresoMision>
)
